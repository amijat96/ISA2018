import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Medicine } from '../model/medicine';
import { baseUrl, httpOptions } from './constants';

@Injectable({
  providedIn: 'root'
})
export class MedicineService {

  constructor(private httpClient: HttpClient) { }

  getMedicines() {
    return this.httpClient.get<Medicine[]>(baseUrl + 'medicines', httpOptions);
  }
}
