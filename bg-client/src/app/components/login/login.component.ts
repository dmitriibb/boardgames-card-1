import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {Base64} from "js-base64";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'bg-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public login = '';
  password = '';

  constructor(private apiService: ApiService,
              private userService: UserService) { }

  ngOnInit(): void {
  }

  loginClick() {
    const auth = 'Basic ' + Base64.encode(this.login + ":" + this.password);
    this.userService.authorize(auth);
  }

}
