import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageExchangeService {

  private errorSubject: Subject<string>  = new Subject();

  constructor() { }

  sendError(errorMessage: string) {
    this.errorSubject.next(errorMessage);
  }

  sendErrorResponse(errorResponse: any) {
    const errorMessage = errorResponse.error.message;
    this.errorSubject.next(errorMessage);
  }

  subscribeForErrors() {
    return this.errorSubject.asObservable();
  }

}
