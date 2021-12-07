export class GameInfoShort {
  id: number;
  name: string;
  secured: boolean;

  formObj(obj: any) {
    Object.assign(this, obj);
    return this;
  }

}
