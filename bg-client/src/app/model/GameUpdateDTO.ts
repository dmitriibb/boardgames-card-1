import {PlayerShortDTO} from "./PlayerShortDTO";
import {CardDTO} from "./CardDTO";

export class GameUpdateDTO {
  id: number;
  me: PlayerShortDTO;
  otherPlayers: PlayerShortDTO[];
  table: CardDTO[];
  activePlayerId: number;
  mainPlayerId: number;
  cardsInDeck: number;

  constructor() {
    this.me = new PlayerShortDTO();
    this.otherPlayers = [];
    this.table = [];
    this.cardsInDeck = 0;
  }

  fromObj(obj) {
    Object.assign(this, obj);
    this.table = this.table.map(c => new CardDTO().fromObj(c));
    this.me = new PlayerShortDTO().fromObj(this.me);
    this.otherPlayers = this.otherPlayers.map(p => new PlayerShortDTO().fromObj(p));
    return this;
  }

}
