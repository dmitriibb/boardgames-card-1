import {Component, Input, OnInit} from '@angular/core';
import {UserShortDTO} from "../../model/UserShortDTO";

@Component({
  selector: 'bg-user-info-short',
  templateUrl: './user-info-short.component.html',
  styleUrls: ['./user-info-short.component.css']
})
export class UserInfoShortComponent implements OnInit {

  @Input('user')
  user: UserShortDTO;

  constructor() { }

  ngOnInit(): void {
  }

}
