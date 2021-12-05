import { Injectable } from '@angular/core';
import {Observable, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private errorSubject: Subject<string>  = new Subject();
  private serverMessagesSubject: Subject<any> = new Subject();
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
    this.serverMessagesSubject.next(message);
  }

  subscribeForMessagesFromServer(): Observable<any> {
    return this.serverMessagesSubject.asObservable();
  }

}
