import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NotificationService} from "./services/notification.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";
import {TestWebSocketAPI} from "./services/testWebSocketAPI";
import {StateService} from "./services/state.service";
import {ApiService} from "./services/api.service";
import {WebSocketAPI} from "./services/WebSocketAPI";
import {UserShortDTO} from "./model/UserShortDTO";
import {Router} from "@angular/router";
import {UserService} from "./services/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy{

  title = 'bg-client';
  errorMessage = '';

  serverMessageType = '';
  serverMessage = '';

  @ViewChild('errorMessageModal')
  errorMessageModal: any;

  @ViewChild('infoMessageModal')
  infoMessageModal: any;

  constructor(private messageExchangeService: NotificationService,
              private modalService: NgbModal,
              private stateService: StateService,
              private api: ApiService,
              private notificationService: NotificationService,
              private webSocketAPI: WebSocketAPI,
              private router: Router,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.messageExchangeService.subscribeForErrors().subscribe(error => {
      this.errorMessage = error;
      this.modalService.open(this.errorMessageModal);
    });

    this.messageExchangeService.subscribeForMessagesFromServer().subscribe(message => {
      this.serverMessageType = message.messageType;
      this.serverMessage = JSON.stringify(message.payload);
      this.modalService.open(this.infoMessageModal);
    });

    this.userService.authorize();
  }

  ngOnDestroy(): void {
    this.userService.logout();
  }

}
