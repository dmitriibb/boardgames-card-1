export class CardDescription {

  id: number;
  name: string;
  type: string;
  points: number;
  coins: number;
  color string;
  houses: number;
  crosses: number;
  anchors: number;
  swords: number;

  fromObj(obj) {
    Object.assign(this, obj);
    return this;
  }

}
