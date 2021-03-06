import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { Clinic } from 'src/app/model/clinic';
import { Observable } from 'rxjs';
import { ReportRequest } from '../model/reportRequest';
import { ReportResponse } from '../model/reportResponse';
import { Examination } from '../model/examination';
import { User } from '../model/user';

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

  getClinicGrade() {
    return this.httpClient.get<number>(baseUrl + 'clinics/' + localStorage.getItem('clinicId') + '/grade', httpOptions);
  }

  getClinicReport(request: ReportRequest) {
    return this.httpClient.put<ReportResponse>(baseUrl + 'clinics/' + localStorage.getItem('clinicId') + '/report', request, httpOptions);
  }

  getClinicExaminations() {
    return this.httpClient.get<Examination[]>(baseUrl + 'clinics/' + localStorage.getItem('clinicId') + '/examinations', httpOptions);
  }

  getClinicPatients() {
    return this.httpClient.get<User[]>(baseUrl + 'clinics/' + localStorage.getItem('clinicId') + '/patients', httpOptions);
  }
}
