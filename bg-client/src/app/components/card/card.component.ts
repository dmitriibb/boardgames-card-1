import {Component, Input, OnInit} from '@angular/core';
import {CardDTO} from "../../model/CardDTO";
import {CardDescription} from "../../model/CardDescription";
import {CardService} from "../../services/card.service";
import {
  CLIENT_MESSAGE_TYPE_BUY_PERSON,
  CLIENT_MESSAGE_TYPE_DESTROY_SHIP,
  CLIENT_MESSAGE_TYPE_SELL_SHIP
} from "../../core/constants";

@Component({
  selector: 'bg-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  @Input('card')
  card: CardDTO;

  constructor(private cardService: CardService) {
    this.card = new CardDTO();
  }

  ngOnInit(): void {
  }

  sellShip() {
    this.cardService.tableCardClick(CLIENT_MESSAGE_TYPE_SELL_SHIP, this.card.id);
  }

  destroyShip() {
    this.cardService.tableCardClick(CLIENT_MESSAGE_TYPE_DESTROY_SHIP, this.card.id);
  }

  buyPerson() {
    this.cardService.tableCardClick(CLIENT_MESSAGE_TYPE_BUY_PERSON, this.card.id);
  }

}
