import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {StateService} from "../../services/state.service";
import {UserShortDTO} from "../../model/UserShortDTO";
import {ApiService} from "../../services/api.service";
import {GameInfoShort} from "../../model/GameInfoShort";
import {NotificationService} from "../../services/notification.service";
import {WebSocketAPI} from "../../services/WebSocketAPI";

@Component({
  selector: 'bg-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user = new UserShortDTO();

  gameName = '';
  gamePassword = ''

  allGames: GameInfoShort[];

  constructor(private router: Router,
              private stateService: StateService,
              private api: ApiService,
              private notificationService: NotificationService,
              private webSocketAPI: WebSocketAPI) {
    this.allGames = [];
  }

  ngOnInit(): void {
    const auth = this.stateService.auth();

    this.api.login(auth).subscribe(res => {
      this.user = new UserShortDTO().formObj(res);
    }, error => {
      console.error(error);
      this.router.navigateByUrl('/login');
    });

    this.loadAllGames();
  }

  logout() {
    this.stateService.logout();
    this.router.navigateByUrl('/login');
    this.notificationService.logoutSubject.next();
  }

  createGameClick() {
    const body = {
      name: this.gameName,
      password: this.gamePassword
    };
    this.api.createNewGame(body).subscribe(response => {
      this.router.navigateByUrl('/game-info/' + response.id)
    }, error => this.notificationService.errorHttpRequest(error));
  }

  private loadAllGames() {
    this.api.getAllGamesInfoShort().subscribe(responseGames => {
      this.allGames = responseGames.map((gameInfo: any) => new GameInfoShort().formObj(gameInfo));
    }, error => this.notificationService.errorHttpRequest(error));
  }

}
