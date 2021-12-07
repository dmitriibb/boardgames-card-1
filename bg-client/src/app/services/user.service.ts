import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {StateService} from "./state.service";
import {Router} from "@angular/router";
import {NotificationService} from "./notification.service";
import {UserShortDTO} from "../model/UserShortDTO";
import {C} from "@angular/cdk/keycodes";
import {WebSocketAPI} from "./WebSocketAPI";
import {CardService} from "./card.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private currentUser: UserShortDTO;

  constructor(private api: ApiService,
              private stateService: StateService,
              private router: Router,
              private notificationService: NotificationService,
              private webSocketApi: WebSocketAPI,) {
    this.currentUser = new UserShortDTO();
  }

  authorize(authParam?) {
    const auth = authParam ? authParam : this.stateService.auth();
    if (!auth) {
      this.router.navigateByUrl('/login');
      return;
    }

    this.api.login(auth).subscribe(res => {
      this.currentUser = new UserShortDTO().formObj(res);
      this.stateService.saveAuth(auth);
      this.notificationService.loginSubject$.next();
      this.webSocketApi._connect();
      this.router.navigateByUrl('/home');
    }, error => this.notificationService.errorHttpRequest(error));
  }

  logout() {
    this.webSocketApi._disconnect();
    this.stateService.logout();
    this.notificationService.logoutSubject.next();
    this.router.navigateByUrl('/login');
  }

  getUser() {
    return this.currentUser;
  }

}
