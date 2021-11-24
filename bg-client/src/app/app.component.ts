import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MessageExchangeService} from "./services/message-exchange.service";
import {NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  title = 'bg-client';
  errorMessage = '';

  @ViewChild('errorMessageModal')
  errorMessageModal: any

  constructor(private messageExchangeService: MessageExchangeService, private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.messageExchangeService.subscribeForErrors().subscribe(error => {
      this.errorMessage = error;
      this.modalService.open(this.errorMessageModal);
    })
  }

}
