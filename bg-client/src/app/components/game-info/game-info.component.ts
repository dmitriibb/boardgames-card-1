import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {Router} from "@angular/router";
import {GameInfo} from "../../model/GameInfo";
import {MessageExchangeService} from "../../services/message-exchange.service";

@Component({
  selector: 'bg-game-info',
  templateUrl: './game-info.component.html',
  styleUrls: ['./game-info.component.css']
})
export class GameInfoComponent implements OnInit {

  gameInfo: GameInfo;

  constructor(private api: ApiService,
              private router: Router,
              private messageExchangeService: MessageExchangeService) { }

  ngOnInit(): void {
    console.log(this);
    this.gameInfo = new GameInfo();
    console.log(this.router.url);
    const gameId = this.router.url.substr(this.router.url.lastIndexOf('/') + 1);
    this.api.getGameInfo(gameId)
      .subscribe(response => {
        this.gameInfo = new GameInfo().formJson(response);
      }, error => this.messageExchangeService.sendErrorResponse(error));
  }

}
