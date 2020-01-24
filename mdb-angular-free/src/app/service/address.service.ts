import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl } from './constants';

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private httpClient: HttpClient) { }

  getCountries() {
    return this.httpClient.get(baseUrl + "countries");
  }

  getCities() {
    return this.httpClient.get(baseUrl + "cities");  
  }

  getCitiesByCountryId(countryId: number) {
    return this.httpClient.get(baseUrl + "cities/" + countryId);
  }
}
