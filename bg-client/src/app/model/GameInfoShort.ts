export class GameInfoShort {
  id: number;
  name: string;
  secured: boolean;

  formObj(json: any) {
    Object.assign(this, json);
    return this;
  }

}
