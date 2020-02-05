import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Report } from '../model/report';
import { baseUrl, httpOptions } from './constants';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private httpClient: HttpClient) { }

  getReport(id: number) {
    return this.httpClient.get<Report>(baseUrl + 'reports/' + id, httpOptions);
  }
}
