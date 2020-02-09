import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Diagnosis } from '../model/diagnosis';
import { baseUrl, httpOptions } from './constants';

@Injectable({
  providedIn: 'root'
})
export class DiagnosisService {

  constructor(private httpClient: HttpClient) { }

  getDiagnoses() {
    return this.httpClient.get<Diagnosis[]>(baseUrl + 'diagnoses', httpOptions);
  }
}
