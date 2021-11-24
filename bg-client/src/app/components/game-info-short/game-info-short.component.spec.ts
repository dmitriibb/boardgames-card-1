import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameInfoShortComponent } from './game-info-short.component';

describe('GameInfoShortComponent', () => {
  let component: GameInfoShortComponent;
  let fixture: ComponentFixture<GameInfoShortComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameInfoShortComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameInfoShortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
