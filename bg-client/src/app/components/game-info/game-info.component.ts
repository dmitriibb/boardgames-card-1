import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {Router} from "@angular/router";
import {GameInfo} from "../../model/GameInfo";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'bg-game-info',
  templateUrl: './game-info.component.html',
  styleUrls: ['./game-info.component.css']
})
export class GameInfoComponent implements OnInit {

  gameInfo: GameInfo;

  constructor(private api: ApiService,
              private router: Router,
              private notificationService: NotificationService) {
    this.gameInfo = new GameInfo();
  }

  ngOnInit(): void {
    this.getGameInfo();
  }

  joinGame() {
    const gameId = this.router.url.substr(this.router.url.lastIndexOf('/') + 1);
    this.api.joinGame(gameId).subscribe(res => this.getGameInfo(),
        error => this.notificationService.errorHttpRequest(error));
  }

  private getGameInfo() {
    const gameId = this.router.url.substr(this.router.url.lastIndexOf('/') + 1);
    this.api.getGameInfo(gameId)
      .subscribe(response => this.gameInfo = new GameInfo().formObj(response),
          error => this.notificationService.errorHttpRequest(error));
  }
}
