import {Component, OnDestroy, OnInit} from '@angular/core';
import {GameService} from "../../services/game.service";
import {Subject} from "rxjs";
import {NotificationService} from "../../services/notification.service";
import {takeUntil} from "rxjs/operators";
import {GameUpdateDTO} from "../../model/GameUpdateDTO";
import {PlayerShortDTO} from "../../model/PlayerShortDTO";
import {WebSocketAPI} from "../../services/WebSocketAPI";
import {CLIENT_MESSAGE_TYPE_DRAW_CARD_FROM_DECK, CLIENT_MESSAGE_TYPE_PASS} from "../../core/constants";
import {StateService} from "../../services/state.service";
import {CardService} from "../../services/card.service";
import {CardDTO} from "../../model/CardDTO";

@Component({
  selector: 'bg-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.css']
})
export class GameComponent implements OnInit, OnDestroy {

  private unsubscribeSubject$ = new Subject();

  game: GameUpdateDTO;
  me: PlayerShortDTO;
  players: PlayerShortDTO[];

  cardsInDeck = 0;

  constructor(private gameService: GameService,
              private notificationService: NotificationService,
              private webSocketAPI: WebSocketAPI,
              private stateService: StateService,
              private cardService: CardService) {

    this.loadGameUpdate(this.notificationService.getGameUpdateValue());
  }

  ngOnInit(): void {
    this.notificationService.gameUpdateDTOSubject()
      .pipe(takeUntil(this.unsubscribeSubject$))
      .subscribe(gameUpdate => this.loadGameUpdate(gameUpdate));
  }

  deckClick() {
    console.log("deck click");
    this.drawCardFromDeck();
  }

  private drawCardFromDeck() {
    const message = {
      type: CLIENT_MESSAGE_TYPE_DRAW_CARD_FROM_DECK
    }
    this.webSocketAPI.send(message);
  }

  ngOnDestroy(): void {
    this.unsubscribeSubject$.next();
    this.unsubscribeSubject$.unsubscribe();
  }

  private loadGameUpdate(gameUpdateDTO) {
    const gameUpdate = this.cardService.enrichGameUpdateDTOCardsWithDescriptionsAndImages(gameUpdateDTO);
    this.stateService.setGameId(gameUpdate.id);
    this.game = gameUpdate;
    this.me = gameUpdate.me;
    this.players = gameUpdate.otherPlayers;
    this.cardsInDeck = gameUpdate.cardsInDeck;
  }

  playerPass() {
    const message = {
      type: CLIENT_MESSAGE_TYPE_PASS
    }
    this.webSocketAPI.send(message);
  }
}
