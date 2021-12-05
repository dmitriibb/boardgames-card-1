import { Component, OnInit } from '@angular/core';
import {TestWebSocketAPI} from "../../services/testWebSocketAPI";

@Component({
  selector: 'bg-my-test',
  templateUrl: './my-test.component.html',
  styleUrls: ['./my-test.component.css']
})
export class MyTestComponent implements OnInit {

  webSocketAPI: TestWebSocketAPI;
  connected = false;
  helloResponse: any;
  name: string;
  auth: string;
  newListeningTopic: string;

  messages: string[] = [];

  constructor() { }

  ngOnInit() {
    this.webSocketAPI = new TestWebSocketAPI(this);
  }

  connect(){
    this.webSocketAPI._connect(this.auth);
  }

  disconnect(){
    this.webSocketAPI._disconnect();
  }

  sendMessage(){
    this.webSocketAPI._send(this.name);
  }

  handleMessage(message){
    console.log(message)
    this.helloResponse = message.body;
  }

  handleMessage2(message) {
    this.messages.push(message.body);
  }


  subscribeForNewTopic() {
    this.webSocketAPI.subscribeForUrl(this.newListeningTopic);
  }
}
