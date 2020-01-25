import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions, TOKEN_KEY } from './constants';
import { Login } from '../model/login';
import { Register } from '../model/register';

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

  storeClinic(clinicId: number, clinicName: string) {
    localStorage.setItem("clinicId", clinicId.toString());
    localStorage.setItem("clinicName", clinicName);
  }

  getCliniId() {
    return localStorage.getItem("clinicId");
  }

  getClinicName() {
    return localStorage.getItem("clinicName");
  }

  register(registration: Register) {
    return this.httpClient.post(baseUrl + "auth/register", registration, httpOptions);
  }

}