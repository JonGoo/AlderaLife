import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import * as moment from 'moment';
import {map} from 'rxjs/operators';
import * as jwt_decode from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  getToken(): string {
    return localStorage.getItem('currentUser');
  }

  getTokenExpirationDate(token: string): Date {
    const decoded = jwt_decode(token);

    if (decoded.exp === undefined) { return null; }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

  isTokenExpired(token?: string): boolean {
    if (!token) { token = this.getToken(); }
    if (!token) { return true; }

    const date = this.getTokenExpirationDate(token);
    if (date === undefined) { return false; }
    return !(date.valueOf() > new Date().valueOf());
  }

  getRoles(token?: string) {
    if (!token) { token = this.getToken(); }

    const decoded = jwt_decode(token);
    if (decoded.role === undefined) { return null; }
    return decoded.role;
  }

  isAdmin(): boolean {
    const roles = this.getRoles();

    for (let x of roles) {
      if (x.name == 'ROLE_ADMIN') {
        return true;
      }
    }
    return false;
  }

  isMod(): boolean {
    const roles = this.getRoles();

    for (let x of roles) {
      if (x.name == 'ROLE_MOD') {
        return true;
      }
    }
    return false;
  }

  getUserId(token?: string): number {
    if (!token) { token = this.getToken(); }

    const decoded = jwt_decode(token);
    if (decoded.sub === undefined) { return null; }
    return decoded.sub;
  }

  isUser(): boolean {
    const roles = this.getRoles();

    for (let x of roles) {
      if (x.name == 'ROLE_USER') {
        return true;
      }
    }
    return false;
  }

  isWhitelist(): boolean {
    const roles = this.getRoles();

    for (let x of roles) {
      if (x.name == 'ROLE_WHITELIST') {
        return true;
      }
    }
    return false;
  }

  login(email: string, password: string ) {
    return this.http.post<any>('/api/auth/signin', {usernameOrEmail: email, password})
      .pipe(map(user => {
        // login successful if there's a jwt token in the response
        if (user) {
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
        }

        return user;
      }));
  }

  register(email: string, password: string, username: string) {
    return this.http.post<any>('/api/auth/signup', {username, email, password})
      .pipe(map(user => {

        return user;
      }));
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
  }
}
