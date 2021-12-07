import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {Router} from "@angular/router";
import {StateService} from "../../services/state.service";
import {Base64} from "js-base64";
import {NotificationService} from "../../services/notification.service";
import {WebSocketAPI} from "../../services/WebSocketAPI";

@Component({
  selector: 'bg-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public login = '';
  password = '';

  constructor(private apiService: ApiService,
              private router: Router,
              private stateService: StateService,
              private notificationService: NotificationService,
              private webSocketAPI: WebSocketAPI) { }

  ngOnInit(): void {
  }

  loginClick() {
    const auth = 'Basic ' + Base64.encode(this.login + ":" + this.password);
    this.apiService.login(auth).subscribe((user) => {
      this.stateService.saveAuth(auth);
      this.router.navigateByUrl('/home');
      this.webSocketAPI._connect();
    }, error => {
      this.notificationService.errorHttpRequest(error);
    })

  }

}
