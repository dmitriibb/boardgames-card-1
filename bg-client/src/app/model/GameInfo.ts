import {PlayerShort} from "./PlayerShort";

export class GameInfo {
  id: number;
  name: string;
  admin: PlayerShort;
  players: PlayerShort[];
  status: string;
  editable: boolean;

  formObj(json: any) {
    Object.assign(this, json);
    this.admin = new PlayerShort().formObj(this.admin);
    this.players = this.players.map(p => new PlayerShort().formObj(p));
    return this;
  }

}
