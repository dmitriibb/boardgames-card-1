import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MyTestComponent} from "../components/my-test/my-test.component";
import {NotificationService} from "./notification.service";
import {StateService} from "./state.service";
import {Injectable} from "@angular/core";
import {CLIENT_MESSAGE_TYPE_ASK_FOR_ACTIVE_GAME_UPDATE} from "../core/constants";
import {ApiService} from "./api.service";

@Injectable({
  providedIn: 'root'
})
export class WebSocketAPI {

  webSocketEndPoint: string = 'http://localhost:8080/ws';
  destinationToRead: string = '/secured/user/queue/messages'
  destinationToSend: string = '/app/user/message';
  stompClient: any;
  sessionId: any;

  useRestAPIForSend = false;

  constructor(private notificationService: NotificationService,
              private stateService: StateService,
              private api: ApiService){

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
      const urlTruncated = url.replace('/websocket', '');
      _this.sessionId = urlTruncated.substr(urlTruncated.lastIndexOf("/") + 1);

      console.log(`connected with session_id: ${_this.sessionId}`)

      _this.stompClient.subscribe(_this.destinationToRead + '-user' + _this.sessionId, function (sdkEvent) {
        console.log(sdkEvent);
        _this.notificationService.messageFromServer(JSON.parse(sdkEvent.body));
      });

      setTimeout(() => _this.send({type: CLIENT_MESSAGE_TYPE_ASK_FOR_ACTIVE_GAME_UPDATE}), 300);

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
    const body = {
      type: message.type,
      payload: (message.payload === undefined || message.payload === null) ? null : JSON.stringify(message.payload)
    }

    if (this.useRestAPIForSend)
      this.sendViaRestAPI(body);
    else
      this.sendViaSocket(body);
  }

  private sendViaSocket(message) {
    const headers = {
      'Authorization': this.stateService.auth()
    }

    const currentGameId = this.stateService.gameId();
    if (currentGameId)
      headers['gameId'] = currentGameId;

    this.stompClient.send(this.destinationToSend, headers, JSON.stringify(message));
  }

  private sendViaRestAPI(message) {
    this.api.sendUserMessage(message).subscribe(res => {},
      error => this.notificationService.errorHttpRequest(error));
  }

}
