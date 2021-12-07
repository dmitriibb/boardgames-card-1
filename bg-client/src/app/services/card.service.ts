import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {CardDescription} from "../model/CardDescription";
import {NotificationService} from "./notification.service";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private cardDescriptionMap: Map<number, CardDescription> = new Map<number, CardDescription>();

  constructor(private api: ApiService, private notificationService: NotificationService) { }

  getDescription(descriptionId) {
    return this.cardDescriptionMap.get(descriptionId);
  }

  private uploadCardDescriptions() {
    this.api.getCardDescriptions().subscribe(res => {
      res.foreach(desc => this.cardDescriptionMap.set(desc.id, new CardDescription().fromObj(desc)));
    }, error => this.notificationService.errorHttpRequest(error));
  }

}
