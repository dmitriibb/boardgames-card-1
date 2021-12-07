import {CardDescription} from "./CardDescription";

export class CardDTO {
  id: number;
  descriptionId: number;
  description: CardDescription;

  constructor() {
    this.description = new CardDescription();
  }

  fromObj(obj) {
    Object.assign(this, obj);
    return this;
  }

}
