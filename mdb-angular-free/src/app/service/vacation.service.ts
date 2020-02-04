import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {baseUrl, httpOptions} from './constants';
import { VacationsByDate } from '../model/vacationsByDate';
import { Vacation } from '../model/vacation';

@Injectable({
  providedIn: 'root'
})
export class VacationService {

  constructor(private httpClient: HttpClient) { }

  getVacations(vacationsRequest: VacationsByDate) {
    vacationsRequest.clinicId = Number(localStorage.getItem('clinicId'));
    return this.httpClient.put<Vacation[]>(baseUrl + 'vacations/all', vacationsRequest, httpOptions);
  }

  approveVacation(vacation: Vacation) {
    return this.httpClient.put<Vacation>(baseUrl + 'vacations/approve', vacation, httpOptions);
  }

  denyVacation(vacation: Vacation) {
    return this.httpClient.put<Vacation>(baseUrl + 'vacations/deny', vacation, httpOptions);
  }
}
