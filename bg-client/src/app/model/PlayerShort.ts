export class PlayerShort {

  id: number;
  name: string;

  formObj(json: any) {
    Object.assign(this, json);
    return this;
  }

}
