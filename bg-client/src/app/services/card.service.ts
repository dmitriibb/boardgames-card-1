import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {CardDescription} from "../model/CardDescription";
import {NotificationService} from "./notification.service";
import {take} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private cardDescriptionMap: Map<number, CardDescription> = new Map<number, CardDescription>();

  constructor(private api: ApiService,
              private notificationService: NotificationService) {
    this.notificationService.loginSubject$
      .pipe(take(1))
      .subscribe(() => this.uploadCardDescriptions());
  }

  getDescription(descriptionId) {
    return this.cardDescriptionMap.get(descriptionId) || new CardDescription();
  }

  uploadCardDescriptions() {
    console.log('uploading card descriptions');
    this.cardDescriptionMap.clear();

    this.api.getCardDescriptions().subscribe(res => {
      res.forEach(desc => {
        const description = new CardDescription().fromObj(desc);
        this.cardDescriptionMap.set(description.id, description);
      });
    }, error => this.notificationService.errorHttpRequest(error));
  }

}
