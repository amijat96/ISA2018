import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { Observable } from 'rxjs';
import { Examination } from '../model/examination';

@Injectable({
  providedIn: 'root'
})
export class ExaminationService {

  constructor(private httpClient: HttpClient) { }

  getExamination(id: number) {
    return this.httpClient.get<Examination>(baseUrl + 'examinations/' + id, httpOptions);
  }
  approveExamination(examination: Examination) {
    return this.httpClient.put(baseUrl + 'examinations/approve-examination/' + examination.examinationId, examination, httpOptions);
  }
  createExamination(examination: Examination) {
    return this.httpClient.post(baseUrl + 'examinations', examination, httpOptions);
  }
  cancelExamination(id: number){
    return this.httpClient.put<Examination>(baseUrl + 'examinations/' + id +'/cancel', httpOptions);
  }
}
