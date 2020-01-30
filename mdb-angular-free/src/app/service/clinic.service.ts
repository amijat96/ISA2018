import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { Clinic } from 'src/app/model/clinic';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClinicService {

  constructor(private httpClient: HttpClient) { }

  getClinic() {
    return this.httpClient.get<Clinic>(baseUrl + 'clinics/' + localStorage.getItem('clinicId'), httpOptions);
  }

  updateClinic(clinic: Clinic) {
    return this.httpClient.put(baseUrl + 'clinics/' + localStorage.getItem('clinicId'), clinic, httpOptions);
  }
}
