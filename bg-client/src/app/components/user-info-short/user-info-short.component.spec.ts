import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserInfoShortComponent } from './user-info-short.component';

describe('UserInfoShortComponent', () => {
  let component: UserInfoShortComponent;
  let fixture: ComponentFixture<UserInfoShortComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserInfoShortComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserInfoShortComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
