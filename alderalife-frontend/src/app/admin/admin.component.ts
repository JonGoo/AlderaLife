import { Component, OnInit } from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {RestService} from "../services/rest.service";

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(public auth: AuthService,
              private router: Router,
              private rest: RestService) {
    if (auth.isTokenExpired() || (!auth.isMod() && !auth.isAdmin())) {
      this.router.navigate(['/home']);
    }
  }

  ngOnInit() {
  }

}
