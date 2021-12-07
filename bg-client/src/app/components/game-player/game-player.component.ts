import {Component, Input, OnInit} from '@angular/core';
import {PlayerShortDTO} from "../../model/PlayerShortDTO";

@Component({
  selector: 'bg-game-player',
  templateUrl: './game-player.component.html',
  styleUrls: ['./game-player.component.css']
})
export class GamePlayerComponent implements OnInit {

  @Input('player')
  player: PlayerShortDTO;

  constructor() {
    this.player = new PlayerShortDTO();
  }

  ngOnInit(): void {
  }


}
