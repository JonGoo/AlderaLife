import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {Router} from '@angular/router';
import {RestService} from '../services/rest.service';
import {PopupComponent} from '../popup/popup.component';
import {MatDialog} from '@angular/material';
import {error} from "@angular/compiler/src/util";

export class Whitelist {
  age: number;
  experience: string;
  steamid: string;
  hasReadRules = false;
  firstNameRp: string;
  lastNameRp: string;
  background: string;
  avenirRp: string;
  user: {
    id: number;
  };
  accepted: boolean;
  refused: boolean;
}

@Component({
  selector: 'app-whitelist',
  templateUrl: './whitelist.component.html',
  styleUrls: ['./whitelist.component.scss']
})
export class WhitelistComponent implements OnInit {
  whitelist: Whitelist = new Whitelist();
  popup: string;

  canWhitelist: boolean;
  messageWhitelist: string;

  constructor(private auth: AuthService,
              private router: Router,
              private rest: RestService,
              public dialog: MatDialog) {
    if (auth.isTokenExpired() || !auth.isUser()) {
      this.router.navigate(['/home']);
    }
    this.rest.canAskWhitelist().subscribe((resp) => {
      if (typeof resp === 'string') {
        this.canWhitelist = false;
        this.messageWhitelist = resp;
        console.log(resp);
      } else if (typeof resp === 'boolean') {
        this.canWhitelist = resp;
      }
    }, (error) => {
      if (typeof error.error.text === 'string') {
        this.canWhitelist = false;
        this.messageWhitelist = error.error.text;
        console.log(error);
      } else if (typeof error === 'boolean') {
        this.canWhitelist = error;
      }
    });
  }

  ngOnInit() {
  }

  openDialog(): void {
    this.dialog.open(PopupComponent, {
      width: '40vw',
      data: {popup: this.popup}
    });
  }

  createWhitelist() {
    console.log(this.auth.getUserId());
    this.whitelist.user = {
      id: this.auth.getUserId()
    };
    console.log(this.whitelist);
    this.rest.sendWhitelist(this.whitelist).subscribe(
      (val) => {
        console.log(val);
        this.popup = 'Votre demande a été envoyée avec succès !';
        this.openDialog();
      },
      response => {
        if (response.status === 500) {
          console.log('Vous n\'avez pas rempli tous les champs');
          this.popup = 'Vous n\'avez pas rempli tous les champs';
          this.openDialog();
        }
      });
  }
}
