import {Component, Input, OnInit} from '@angular/core';
import {GameInfoShort} from "../../model/GameInfoShort";
import {Router} from "@angular/router";

@Component({
  selector: 'bg-game-info-short',
  templateUrl: './game-info-short.component.html',
  styleUrls: ['./game-info-short.component.css']
})
export class GameInfoShortComponent implements OnInit {

  @Input('gameInfoShort')
  gameInfo: GameInfoShort;

  secured = false;

  constructor(private router: Router) {
    this.gameInfo = new GameInfoShort();
  }

  ngOnInit(): void {
    this.secured = this.gameInfo.secured;
  }

  openGameInfo() {
    this.router.navigateByUrl('/game-info/' + this.gameInfo.id);
  }
}
