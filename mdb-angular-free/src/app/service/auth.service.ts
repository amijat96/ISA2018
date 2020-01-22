import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions, TOKEN_KEY } from './constants';
import { Login } from '../model/login';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private httpClient: HttpClient) { }

  login(login: Login) {
    return this.httpClient.post(baseUrl + "auth/login", login, httpOptions);
  }

  storeToken(token: string) {
    localStorage.setItem(TOKEN_KEY, token);
  }

  getToken() {
    return localStorage.getItem(TOKEN_KEY);
  }
}