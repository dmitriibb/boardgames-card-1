import {CardDTO} from "./CardDTO";

export class PlayerShortDTO {
  id: number;

  name: string;

  main: boolean;
  active: boolean;

  order: number;
  coins: number;
  points: number;
  swords: number;
  crosses: number;
  houses: number;
  anchors: number;

  cards: CardDTO[] = [];

  fromObj(obj) {
    Object.assign(this, obj);
    this.cards = this.cards.map(c => new CardDTO().fromObj(c));
    return this;
  }

}
