export class UserShortDTO {
 id: number;
 name: string;
 email: string;

  formObj(json: any) {
    Object.assign(this, json);
    return this;
  }

}
