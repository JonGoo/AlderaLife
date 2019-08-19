import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogModule, MatDialogRef} from '@angular/material';
import {AuthService} from '../services/auth.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {


  register = false;
  registerButton = 'Se connecter';

  email: string;
  confirmEmail: string;
  username: string;
  password: string;
  confirmPassword: string;
  submitted = false;

  error: string;

  constructor(public dialogRef: MatDialogRef<LoginComponent>, public auth: AuthService) {
  }

  ngOnInit() {
    // reset login status
    this.auth.logout();
  }

  closePopup(): void {
    this.dialogRef.close();
  }

  authenticate() {
    this.submitted = true;
    if (!this.register) {
      this.auth.login(this.email, this.password)
        .pipe(first())
        .subscribe(
          data => {
            console.log(data);
            this.closePopup();
          },
          error => {
            console.log(error);
            if (error.error.message == 'Bad credentials') {
              this.error = 'Adresse mail ou mot de passe incorrect !';
            }
          });
    } else {
      if (this.password === this.confirmPassword) {
        if (this.email === this.confirmEmail) {
          this.auth.register(this.email, this.password, this.username)
            .pipe(first())
            .subscribe(
              data => {
                this.error = null;
                console.log(data);
                this.closePopup();
              },
              error => {
                console.log(error);
                this.error =  error.error.message;
              });
        } else {
          this.error = 'Le mail et le mail de confirmation ne sont pas identique';
          console.log(this.error);
        }
      } else {
        this.error = 'Le mot de passe et le mot de passe de confirmation ne sont pas identique';
        console.log(this.error);
      }
    }
  }

}
