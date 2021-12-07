import {Component, Input, OnInit} from '@angular/core';
import {GameUpdateDTO} from "../../model/GameUpdateDTO";

@Component({
  selector: 'bg-game-table',
  templateUrl: './game-table.component.html',
  styleUrls: ['./game-table.component.css']
})
export class GameTableComponent implements OnInit {

  @Input('gameUpdate')
  gameUpdate: GameUpdateDTO;

  constructor() {
    this.gameUpdate = new GameUpdateDTO();
  }

  ngOnInit(): void {
  }

}
