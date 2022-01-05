import { Injectable } from '@angular/core';
import {ApiService} from "./api.service";
import {CardDescription} from "../model/CardDescription";
import {NotificationService} from "./notification.service";
import {take} from "rxjs/operators";
import {WebSocketAPI} from "./WebSocketAPI";
import {CardClickDTO} from "../model/CardClickDTO";
import {DomSanitizer} from "@angular/platform-browser";
import {GameUpdateDTO} from "../model/GameUpdateDTO";
import {CardDTO} from "../model/CardDTO";

@Injectable({
  providedIn: 'root'
})
export class CardService {

  private cardDescriptionMap: Map<number, CardDescription> = new Map<number, CardDescription>();
  private cardImagesMap: Map<string, any> = new Map<string, any>();

  constructor(private api: ApiService,
              private notificationService: NotificationService,
              private wevSocketAPI: WebSocketAPI,
              private sanitizer: DomSanitizer) {
    /*this.notificationService.loginSubject$
      //.pipe(take(1))
      .subscribe(() => this.uploadCardDescriptions());*/
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
      this.uploadCardImages();
    }, error => this.notificationService.errorHttpRequest(error));
  }

  uploadCardImages() {
    console.log('uploading card images');
    this.api.getCardImages().subscribe(res => {
      res.forEach(cardImage => {
        this.cardImagesMap.set(cardImage.name, cardImage.value);
      });
      this.putImagesIntoDescriptions();
    })
  }

  putImagesIntoDescriptions() {
    this.cardDescriptionMap.forEach((value) => {
      const image = this.cardImagesMap.get(value.imageName);
      if (image) {
        value.image = this.sanitizer.bypassSecurityTrustUrl('data:image/png;charset=utf-8;base64,' + image);
        value.hasImage = true;
      }
    });
  }


  tableCardClick(messageType, cardId) {
    const message = {
      type: messageType,
      payload: new CardClickDTO(cardId)
    }
    this.wevSocketAPI.send(message);
  }

  enrichGameUpdateDTOCardsWithDescriptionsAndImages(gameUpdateDTO: GameUpdateDTO): GameUpdateDTO {
    gameUpdateDTO.table.forEach(card => {
      card.description = this.getDescription(card.descriptionId);
      card.clickable = true;
    });

    gameUpdateDTO.me.cards.forEach(card => {
      card.description = this.getDescription(card.descriptionId);
      card.clickable = false;
    })

    gameUpdateDTO.otherPlayers.forEach(player => {
      player.cards.forEach(card => {
        card.description = this.getDescription(card.descriptionId);
        card.clickable = false;
      })
    })

    return gameUpdateDTO;
  }





}
