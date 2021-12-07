import { Injectable } from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';
import {GameUpdateDTO} from "../model/GameUpdateDTO";
import {SERVER_MESSAGE_TYPE_GAME_UPDATE} from "../core/constants";
import {UserShortDTO} from "../model/UserShortDTO";
import {PlayerShortDTO} from "../model/PlayerShortDTO";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private errorSubject: Subject<string>  = new Subject();
  private serverMessagesSubject: Subject<any> = new Subject();
  private gameUpdateSubject$: BehaviorSubject<GameUpdateDTO> = new BehaviorSubject<GameUpdateDTO>(new GameUpdateDTO());
  loginSubject$: Subject<void> = new Subject();
  logoutSubject: Subject<void> = new Subject();

  constructor() { }

  errorMessage(errorMessage: string) {
    this.errorSubject.next(errorMessage);
  }

  errorHttpRequest(errorResponse: any) {
    const errorMessage = errorResponse.error.message;
    this.errorSubject.next(errorMessage);
  }

  subscribeForErrors() {
    return this.errorSubject.asObservable();
  }

  messageFromServer(message) {
    const type = message.type;
    const payload = message.payload;

    if (type === SERVER_MESSAGE_TYPE_GAME_UPDATE)
      this.gameUpdateSubject$.next(new GameUpdateDTO().fromObj(payload));
    else
      this.serverMessagesSubject.next(message);
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

}
