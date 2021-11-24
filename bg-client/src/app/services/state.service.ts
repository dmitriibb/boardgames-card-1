import { Injectable } from '@angular/core';
import {AUTHORIZATION_VALUE, STORAGE_USER} from "../core/constants";

@Injectable({
  providedIn: 'root'
})
export class StateService {

  constructor() {}

  login(user: any, auth: string) {
    localStorage.setItem(STORAGE_USER, JSON.stringify(user));
    localStorage.setItem(AUTHORIZATION_VALUE, auth);
  }

  logout() {
    localStorage.removeItem(STORAGE_USER);
    localStorage.removeItem(AUTHORIZATION_VALUE);
  }

  currentUser() {
    const userJson = localStorage.getItem(STORAGE_USER);
    return userJson ? JSON.parse(userJson) : null;
  }

  auth(): string {
    return localStorage.getItem(AUTHORIZATION_VALUE) || '';
  }

}
