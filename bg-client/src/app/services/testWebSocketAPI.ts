import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {MyTestComponent} from "../components/my-test/my-test.component";

export class TestWebSocketAPI {

  webSocketEndPoint: string = 'http://localhost:8080/ws';
  topic: string = "/topic/hello";
  topicMessage: string = '/topic/user/message'
  stompClient: any;
  testComponent: MyTestComponent;
  sessionId: any;
  auth: string = '';

  constructor(testComponent: MyTestComponent){
    this.testComponent = testComponent;
  }

  _connect(auth: string) {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const _this = this;

    const headers = {
      Authorization: auth
    }

    _this.stompClient.connect(headers, function (frame) {
      let url = _this.stompClient.ws._transport.url;
      console.log(url);

      _this.sessionId = url.substr(url.lastIndexOf("/") + 1);

      console.log(_this.sessionId)

      _this.stompClient.subscribe(_this.topic, function (sdkEvent) {
        _this.onMessageReceived(sdkEvent);
      });

      _this.stompClient.subscribe('/secured/user/queue/message-user' + _this.sessionId, function (sdkEvent) {
        console.log(sdkEvent);
        _this.testComponent.handleMessage2(sdkEvent);
      });

      //_this.stompClient.reconnect_delay = 2000;
      _this.testComponent.connected = true;
    }, this.errorCallBack);
  };

  _disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
      this.testComponent.connected = false;
    }
    console.log("Disconnected");
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log("errorCallBack -> " + error)
    setTimeout(() => {
      this._connect(this.auth);
    }, 5000);
  }

  /**
   * Send message to sever via web socket
   * @param {*} message
   */
  _send(message) {
    console.log("calling logout api via web socket");
    const body = "message from client: " + message;

    const headers = {
      Authorization: this.auth
    }

    this.stompClient.send("/app/hello", {}, body);

    this.stompClient.send("/app/secured/test", {}, body);

  }

  subscribeForUrl(url: string) {
    const _this = this;
    this.stompClient.subscribe(url, function (sdkEvent) {
      console.log(sdkEvent);
      _this.testComponent.handleMessage2(sdkEvent);
    });
  }

  onMessageReceived(message) {
    console.log("Message Recieved from Server :: " + message);
    this.testComponent.handleMessage(message);
  }
}
