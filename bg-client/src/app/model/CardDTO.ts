export class CardDTO {
  id: number;
  description: string;

  fromObj(obj) {
    Object.assign(this, obj);
    return this;
  }

}
