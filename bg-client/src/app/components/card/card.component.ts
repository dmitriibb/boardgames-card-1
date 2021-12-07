import {Component, Input, OnInit} from '@angular/core';
import {CardDTO} from "../../model/CardDTO";
import {CardDescription} from "../../model/CardDescription";

@Component({
  selector: 'bg-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {

  @Input('card')
  card: CardDTO;

  constructor() {
    this.card = new CardDTO();
  }

  ngOnInit(): void {
  }

}
