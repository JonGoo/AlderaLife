import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from './auth.service';
import {Whitelist} from '../whitelist/whitelist.component';

@Injectable({
  providedIn: 'root'
})
export class RestService {


  private prefix = '/api/';

  constructor(private http: HttpClient,
              private auth: AuthService) { }

  sendWhitelist(whitelist: Whitelist) {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type':  'application/json',
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
    return this.http.post<any>(this.prefix + 'whitelist/', whitelist, httpOptions);
  }

  getWhitelistList() {
    const httpOptions = {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
    return this.http.get(this.prefix + 'whitelist/', httpOptions);
  }

  canAskWhitelist() {
    const httpOptions = {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
    return this.http.get(this.prefix + 'whitelist/canWhitelist/' + this.auth.getUserId(), httpOptions);
  }

  acceptWhitelist(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
    return this.http.get(this.prefix + 'whitelist/accept/' + id, httpOptions);
  }

  refuseWhitelist(id: number) {
    const httpOptions = {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
    return this.http.get(this.prefix + 'whitelist/refuse/' + id, httpOptions);
  }
}
