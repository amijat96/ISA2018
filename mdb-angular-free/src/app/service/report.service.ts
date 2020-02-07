import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Report } from '../model/report';
import { baseUrl, httpOptions } from './constants';
import { DoctorReportRequest } from '../model/doctorReportRequest';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private httpClient: HttpClient) { }

  getReport(id: number) {
    return this.httpClient.get<Report>(baseUrl + 'reports/' + id, httpOptions);
  }

  createReport(id: number, report: DoctorReportRequest) {
    return this.httpClient.post<Report>(baseUrl + 'reports/' + id, report, httpOptions);
  }
}
