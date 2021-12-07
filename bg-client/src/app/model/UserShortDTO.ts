export class UserShortDTO {
 id: number;
 name: string;
 email: string;

  formObj(obj: any) {
    Object.assign(this, obj);
    return this;
  }

}
