import {CardDescription} from "./CardDescription";

export class CardDTO {
  id: number;
  descriptionId: number;
  description: CardDescription;
  clickable: boolean = true;

  constructor() {
    this.description = new CardDescription();
  }

  fromObj(obj) {
    Object.assign(this, obj);
    return this;
  }

}
