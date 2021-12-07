import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MyTestComponent} from "../components/my-test/my-test.component";
import {NotificationService} from "./notification.service";
import {StateService} from "./state.service";
import {Injectable} from "@angular/core";
import {CLIENT_MESSAGE_TYPE_ASK_FOR_ACTIVE_GAME_UPDATE} from "../core/constants";

@Injectable({
  providedIn: 'root'
})
export class WebSocketAPI {

  webSocketEndPoint: string = 'http://localhost:8080/ws';
  destinationToRead: string = '/secured/user/queue/messages'
  destinationToSend: string = '/app/user/message';
  notificationService: NotificationService;
  stateService: StateService;
  stompClient: any;
  sessionId: any;

  constructor(notificationService: NotificationService, stateService: StateService){
    this.notificationService = notificationService;
    this.stateService = stateService;
    this.notificationService.logoutSubject.subscribe(() => this._disconnect());
  }

  _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;

    const headers = {
      'Authorization': this.stateService.auth()
    }

    _this.stompClient.connect(headers, function (frame) {
      let url = _this.stompClient.ws._transport.url;
      _this.sessionId = url.substr(url.lastIndexOf("/") + 1);

      console.log(_this.sessionId)

      _this.stompClient.subscribe(_this.destinationToRead + '-user' + _this.sessionId, function (sdkEvent) {
        console.log(sdkEvent);
        _this.notificationService.messageFromServer(JSON.parse(sdkEvent.body));
      });

      _this.send({type: CLIENT_MESSAGE_TYPE_ASK_FOR_ACTIVE_GAME_UPDATE})

    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
      this.sessionId = null;
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.error(error);
    this.notificationService.errorMessage(error);
    setTimeout(() => {
      this._connect();
    }, 5000);
  }

  /**
   * Send message to sever via web socket
   * @param {*} message
   */
  send(message) {

    const headers = {
      'Authorization': this.stateService.auth()
    }

    const body = {
      type: message.type,
      payload: (message.payload === undefined || message.payload === null) ? null : JSON.stringify(message.payload)
    }

    this.stompClient.send(this.destinationToSend, headers, JSON.stringify(body));
  }

}
