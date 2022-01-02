export class CardDescription {

  id: number;
  name: string;
  type: string;
  points: number;
  coins: number;
  color: string;
  houses: number;
  crosses: number;
  anchors: number;
  swords: number;
  imageName: string;
  hasImage: boolean = false;
  image: any;

  fromObj(obj) {
    Object.assign(this, obj);
    return this;
  }

}
