export class PlayerShort {

  id: number;
  name: string;

  formJson(json: any) {
    Object.assign(this, json);
    return this;
  }

}
