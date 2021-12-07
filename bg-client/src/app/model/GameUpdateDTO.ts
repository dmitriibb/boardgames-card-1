import {PlayerShortDTO} from "./PlayerShortDTO";
import {CardDTO} from "./CardDTO";

export class GameUpdateDTO {
  me: PlayerShortDTO;
  otherPlayers: PlayerShortDTO[];
  table: CardDTO[];
  activePlayerId: number;
  mainPlayerId: number;
  cardsInDeck: number;

  fromObj(obj) {
    Object.assign(this, obj);
    this.table = this.table.map(c => new CardDTO().fromObj(c));
    this.me = new PlayerShortDTO().fromObj(this.me);
    this.otherPlayers = this.otherPlayers.map(p => new PlayerShortDTO().fromObj(p));
    return this;
  }

}
