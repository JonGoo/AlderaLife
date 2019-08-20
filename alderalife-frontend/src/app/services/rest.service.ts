import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from './auth.service';
import {Whitelist} from '../whitelist/whitelist.component';

@Injectable({
  providedIn: 'root'
})
export class RestService {


  httpOptions;
  private prefix = '/api/';

  constructor(private http: HttpClient,
              private auth: AuthService) {
    this.httpOptions = {
      headers: new HttpHeaders({
        Authorization: 'Bearer ' + JSON.parse(this.auth.getToken()).accessToken
      })
    };
  }

  sendWhitelist(whitelist: Whitelist) {
    this.httpOptions.headers.set('Content-Type', 'application/json');
    return this.http.post<any>(this.prefix + 'whitelist/', whitelist, this.httpOptions);
  }

  getWhitelistList() {
    return this.http.get(this.prefix + 'whitelist/', this.httpOptions);
  }

  canAskWhitelist() {
    return this.http.get(this.prefix + 'whitelist/canWhitelist/' + this.auth.getUserId(), this.httpOptions);
  }

  acceptWhitelist(id: number) {
    return this.http.put(this.prefix + 'whitelist/accept/' + id, null, this.httpOptions);
  }

  refuseWhitelist(id: number) {
    return this.http.put(this.prefix + 'whitelist/refuse/' + id, null, this.httpOptions);
  }

  getUserList() {
    return this.http.get(this.prefix + 'user/', this.httpOptions);
  }

  getRoleList() {
    return this.http.get(this.prefix + 'role/', this.httpOptions);
  }

  setUserRole(id, role) {
    return this.http.put(this.prefix + 'user/setrole/' + id, role, this.httpOptions);
  }

  getServersList() {
    return this.http.get(this.prefix + 'monitoring/getServers/', this.httpOptions);
  }

  isServerOnline(name: string) {
    return this.http.post(this.prefix + 'monitoring/isOnline/', name, this.httpOptions);
  }

  shutdownServer(name: string) {
    return this.http.post(this.prefix + 'monitoring/shutdown/', name, this.httpOptions);
  }

  startServer(name: string) {
    return this.http.post(this.prefix + 'monitoring/start/', name, this.httpOptions);
  }

  sendCommand(command: string, server: string) {
    const body = {
      command,
      server
    }
    return this.http.post(this.prefix + 'monitoring/command/', body, this.httpOptions);
  }

  getPlayers() {
    return this.http.get('/server/players.json');
  }

  confirmEmail(token: string){
    return this.http.get(this.prefix + 'auth/confirm?token=' + token);
  }
}
