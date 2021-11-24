import {PlayerShort} from "./PlayerShort";

export class GameInfo {
  id: number;
  name: string;
  admin: PlayerShort;
  players: PlayerShort[];
  status: string;
  editable: boolean;

  formJson(json: any) {
    Object.assign(this, json);
    this.players = this.players.map(p => new PlayerShort().formJson(p));
    return this;
  }

}
