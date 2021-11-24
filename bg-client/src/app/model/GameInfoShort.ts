export class GameInfoShort {
  id: number;
  name: string;
  secured: boolean;

  fromJson(json: any) {
    Object.assign(this, json);
    return this;
  }

}
