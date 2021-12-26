import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {GameUpdateDTO} from "../model/GameUpdateDTO";
import {
  SERVER_MESSAGE_TYPE_ERROR,
  SERVER_MESSAGE_TYPE_GAME_UPDATE,
  SERVER_MESSAGE_TYPE_SHORT_MESSAGE
} from "../core/constants";
import {UserShortDTO} from "../model/UserShortDTO";
import {PlayerShortDTO} from "../model/PlayerShortDTO";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private errorSubject$: Subject<string>  = new Subject();
  private tmpMessageSubject$: Subject<string>  = new Subject();
  private serverMessagesSubject: Subject<any> = new Subject();
  private gameUpdateSubject$: BehaviorSubject<GameUpdateDTO> = new BehaviorSubject<GameUpdateDTO>(new GameUpdateDTO());
  loginSubject$: Subject<void> = new Subject();
  logoutSubject: Subject<void> = new Subject();

  constructor() { }

  errorMessage(errorMessage: string) {
    this.errorSubject$.next(errorMessage);
  }

  errorHttpRequest(errorResponse: any) {
    const errorMessage = errorResponse.error.message;
    this.errorSubject$.next(errorMessage);
  }

  subscribeForErrors() {
    return this.errorSubject$.asObservable();
  }

  messageFromServer(message) {
    const type = message.type;
    const payload = message.payload;

    switch (type) {
      case SERVER_MESSAGE_TYPE_GAME_UPDATE:
        this.gameUpdateSubject$.next(new GameUpdateDTO().fromObj(payload));
        break;

      case SERVER_MESSAGE_TYPE_ERROR:
        this.errorSubject$.next(payload);
        break;

      case SERVER_MESSAGE_TYPE_SHORT_MESSAGE:
        this.tmpMessageSubject$.next(payload);
        break;

      default:
        this.serverMessagesSubject.next(payload);
    }

  }

  subscribeForMessagesFromServer(): Observable<any> {
    return this.serverMessagesSubject.asObservable();
  }

  gameUpdateDTOSubject() {
    return this.gameUpdateSubject$.asObservable();
  }

  getGameUpdateValue() {
    return this.gameUpdateSubject$.value;
  }

  subscribeForTmpMessages() {
    return this.tmpMessageSubject$.asObservable();
  }

  tmpMessage(message) {
    this.tmpMessageSubject$.next(message);
  }

}
