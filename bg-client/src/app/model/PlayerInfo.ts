export class PlayerInfo {

  id: number;
  name: string;

  formObj(obj: any) {
    Object.assign(this, obj);
    return this;
  }

}
