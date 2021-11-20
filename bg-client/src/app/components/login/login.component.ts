import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'bg-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public login = '';
  password = '';


  constructor() { }

  ngOnInit(): void {
  }

  loginClick() {
    console.log("login: " + this.login + " - " + this.password);
  }

}
