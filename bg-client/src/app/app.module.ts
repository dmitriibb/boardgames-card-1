import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import { UserInfoShortComponent } from './components/user-info-short/user-info-short.component';
import { GameInfoShortComponent } from './components/game-info-short/game-info-short.component';
import { GameInfoComponent } from './components/game-info/game-info.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import { MyTestComponent } from './components/my-test/my-test.component';
import { GameComponent } from './components/game/game.component';
import { GameTableComponent } from './components/game-table/game-table.component';
import { CardComponent } from './components/card/card.component';
import { GamePlayerComponent } from './components/game-player/game-player.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    UserInfoShortComponent,
    GameInfoShortComponent,
    GameInfoComponent,
    MyTestComponent,
    GameComponent,
    GameTableComponent,
    CardComponent,
    GamePlayerComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        NgbModule,
        BrowserAnimationsModule,
        MatIconModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
