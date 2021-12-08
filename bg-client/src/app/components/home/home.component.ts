import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {StateService} from "../../services/state.service";
import {UserShortDTO} from "../../model/UserShortDTO";
import {ApiService} from "../../services/api.service";
import {GameInfoShort} from "../../model/GameInfoShort";
import {NotificationService} from "../../services/notification.service";
import {WebSocketAPI} from "../../services/WebSocketAPI";
import {UserService} from "../../services/user.service";
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
  selector: 'bg-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  private unsubscribeSubject$ = new Subject();

  user = new UserShortDTO();

  gameName = '';
  gamePassword = ''

  allGames: GameInfoShort[];

  constructor(private router: Router,
              private api: ApiService,
              private notificationService: NotificationService,
              private webSocketAPI: WebSocketAPI,
              private userService: UserService) {
    this.allGames = [];
    this.notificationService.loginSubject$
      .pipe(takeUntil(this.unsubscribeSubject$))
      .subscribe(() => this.user = this.userService.getUser());
  }

  ngOnInit(): void {
    this.user = this.userService.getUser();
    this.loadAllGames();
  }

  logout() {
    this.userService.logout();
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

  ngOnDestroy(): void {
    this.unsubscribeSubject$.next();
  }

}
