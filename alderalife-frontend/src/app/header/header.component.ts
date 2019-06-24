
import {MatDialog, MatDialogModule, MatDialogRef} from '@angular/material';
import {LoginComponent} from '../login/login.component';
import {Component} from '@angular/core';
import {AuthService} from '../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(public dialog: MatDialog, public auth: AuthService) { }

  connexionPopUp(): void {
    this.dialog.open(LoginComponent, {
      width: '25vw'
    });
  }

  logout() {
    this.auth.logout();
  }

}
