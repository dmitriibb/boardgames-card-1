import { Injectable } from '@angular/core';
import {AUTHORIZATION_VALUE, STORAGE_USER} from "../core/constants";
import {Location} from "@angular/common";

@Injectable({
  providedIn: 'root'
})
export class StateService {

  private _auth: string;
  private _authDima = 'Basic ZGltYUB0ZXN0LmNvbTpxd2VydHk=';
  private _authJohn = 'Basic am9obkB0ZXN0LmNvbTpxd2VydHky';
  private currentGameId = null;

  constructor(private location: Location) {
    const loc = this.location['_platformLocation'].location;
    if (loc.port === '4200')
      this._auth = this._authDima;
    else
      this._auth = this._authJohn;
  }

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
