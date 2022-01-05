import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {PlayerShortDTO} from "../../model/PlayerShortDTO";

@Component({
  selector: 'bg-game-player',
  templateUrl: './game-player.component.html',
  styleUrls: ['./game-player.component.css']
})
export class GamePlayerComponent implements OnInit, OnChanges {

  @ViewChild('playerCardsCarousel')
  playerCardsCarousel: ElementRef;

  @Input('player')
  player: PlayerShortDTO;

  constructor() {
    this.player = new PlayerShortDTO();
  }

  ngOnChanges(changes: SimpleChanges): void {
        this.setActivePlayerCard();
  }

  ngOnInit(): void {
  }

  setActivePlayerCard() {
    setTimeout(() => {
      const children = this.playerCardsCarousel.nativeElement.children;
      if (children && children.length) {
        const firstChildren = children[0];
        firstChildren.classList.add("active");
      }
      console.log(this.playerCardsCarousel);
    }, 200);

  }


}
