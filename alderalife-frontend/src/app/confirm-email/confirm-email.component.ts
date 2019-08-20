import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {RestService} from "../services/rest.service";

@Component({
  selector: 'app-confirm-email',
  templateUrl: './confirm-email.component.html',
  styleUrls: ['./confirm-email.component.scss']
})
export class ConfirmEmailComponent implements OnInit {

  token: string;
  constructor(private activatedRoute: ActivatedRoute, private rest: RestService) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token = params.token;
    });
  }

  ngOnInit() {
  }

  confirmMail() {
    this.rest.confirmEmail(this.token);
  }

}
