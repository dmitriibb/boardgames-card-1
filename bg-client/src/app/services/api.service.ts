import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {BASE_URL} from "../core/constants";
import {StateService} from "./state.service";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http: HttpClient, private stateService: StateService) { }

  login(auth: string) {
    const headers = {"Authorization": auth};
    return this.http.get(BASE_URL + "/user/login", {headers: headers});
  }

  createNewGame(body: any): Observable<any> {
    return this.http.post(BASE_URL + '/game/new', body, {headers: this.getBaseHeaders()});
  }

  getGameInfo(gameId: any): Observable<any> {
    return this.http.get(BASE_URL + '/game/' + gameId, {headers: this.getBaseHeaders()})
  }

  getAllGamesInfoShort(): Observable<any> {
    return this.http.get(BASE_URL + '/game/all', {headers: this.getBaseHeaders()});
  }

  joinGame(gameId) {
    return this.http.put(BASE_URL + '/game/join/' + gameId, {headers: this.getBaseHeaders()});
  }

  getCardDescriptions(): Observable<any> {
    return this.http.get(BASE_URL + '/card/descriptions', {headers: this.getBaseHeaders()});
  }

  private getBaseHeaders() {
    return {
      "Authorization": this.stateService.auth()
    };
  }

}
