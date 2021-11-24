import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from "@angular/router";
import {StateService} from "../../services/state.service";
import {UserShortDTO} from "../../model/UserShortDTO";
import {ApiService} from "../../services/api.service";
import {GameInfoShort} from "../../model/GameInfoShort";
import {MessageExchangeService} from "../../services/message-exchange.service";

@Component({
  selector: 'bg-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  user = new UserShortDTO();

  gameName = '';
  password = ''

  allGames: GameInfoShort[];

  constructor(private router: Router,
              private stateService: StateService,
              private api: ApiService,
              private messageExchangeService: MessageExchangeService) {
    this.allGames = [];
  }

  ngOnInit(): void {
    const currentUSer = this.stateService.currentUser();
    if (!currentUSer) {
      this.router.navigateByUrl('/login');
      return;
    }
    this.user = currentUSer;
    this.loadAllGames();
  }

  logout() {
    this.stateService.logout();
    this.router.navigateByUrl('/login');
  }

  createGameClick() {
    const body = {
      name: this.gameName,
      password: this.password
    };
    this.api.createNewGame(body).subscribe(response => {
      console.log(response);
      this.router.navigateByUrl('/game-info/' + response.id)
    })
  }

  private loadAllGames() {
    this.api.getAllGamesInfoShort().subscribe(responseGames => {
      this.allGames = responseGames.map((gameInfo: any) => new GameInfoShort().fromJson(gameInfo));
    }, error => this.messageExchangeService.sendErrorResponse(error));
  }

}
