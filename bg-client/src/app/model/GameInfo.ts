import {PlayerInfo} from "./PlayerInfo";

export class GameInfo {
  id: number;
  name: string;
  admin: PlayerInfo;
  players: PlayerInfo[];
  status: string;
  editable: boolean;

  formObj(obj: any) {
    Object.assign(this, obj);
    this.admin = new PlayerInfo().formObj(this.admin);
    this.players = this.players.map(p => new PlayerInfo().formObj(p));
    return this;
  }

}
