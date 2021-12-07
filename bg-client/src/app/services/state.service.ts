import { Injectable } from '@angular/core';
import {AUTHORIZATION_VALUE, STORAGE_USER} from "../core/constants";

@Injectable({
  providedIn: 'root'
})
export class StateService {

  private _auth = 'Basic ZGltYUB0ZXN0LmNvbTpxd2VydHk=';
  private currentGameId = null;

  constructor() {}

  saveAuth(auth: string) {
    //localStorage.setItem(STORAGE_USER, JSON.stringify(user));
    //localStorage.setItem(AUTHORIZATION_VALUE, auth);
    this._auth = auth;
  }

  logout() {
    //localStorage.removeItem(STORAGE_USER);
    //localStorage.removeItem(AUTHORIZATION_VALUE);
    this._auth = '';
  }

  auth(): string {
    //return localStorage.getItem(AUTHORIZATION_VALUE) || '';
    return this._auth;
  }

  setGameId(gameId) {
    this.currentGameId = gameId;
  }

  gameId() {
    return this.currentGameId;
  }

}
