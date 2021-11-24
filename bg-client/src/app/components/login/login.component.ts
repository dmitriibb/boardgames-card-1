import { Component, OnInit } from '@angular/core';
import {ApiService} from "../../services/api.service";
import {Router} from "@angular/router";
import {StateService} from "../../services/state.service";
import {Base64} from "js-base64";

@Component({
  selector: 'bg-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  public login = '';
  password = '';


  constructor(private apiService: ApiService, private router: Router, private stateService: StateService) { }

  ngOnInit(): void {
  }

  loginClick() {
    console.log('login: ' + this.login + ':' + this.password);
    const auth = 'Basic ' + Base64.encode(this.login + ":" + this.password);
    this.apiService.login(auth).subscribe((user) => {
      console.log(user);

      this.stateService.login(user, auth);
      this.router.navigateByUrl('/home');
    }, error => {
      console.log(error);
    })

  }

}
