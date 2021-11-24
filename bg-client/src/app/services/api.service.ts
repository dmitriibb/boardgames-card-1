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
    const headers = {"Authorization": this.stateService.auth()};
    return this.http.post(BASE_URL + '/game/new', body, {headers: headers});
  }

  getGameInfo(gameId: any): Observable<any> {
    const headers = {"Authorization": this.stateService.auth()};
    return this.http.get(BASE_URL + '/game/' + gameId, {headers: headers})
  }

  getAllGamesInfoShort(): Observable<any> {
    const headers = {"Authorization": this.stateService.auth()};
    return this.http.get(BASE_URL + '/game/all', {headers: headers});
  }

}
