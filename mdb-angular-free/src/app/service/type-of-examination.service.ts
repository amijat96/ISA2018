import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { TypeOfExamination } from '../model/typeOfExamination';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class TypeOfExaminationService {

  constructor(private httpClient: HttpClient) { }

  getTypesOfExamination():Observable<TypeOfExamination[]> {
    return this.httpClient.get<TypeOfExamination[]>(baseUrl + 'types-of-examination', httpOptions);
  }

  createTypeOfExamination(typeOfExamination: TypeOfExamination) {
    return this.httpClient.post(baseUrl + 'types-of-examination', typeOfExamination, httpOptions);
  }

  updateTypeOfExamination(typeOfExamination: TypeOfExamination) {
    return this.httpClient.put(baseUrl + 'types-of-examination/' + typeOfExamination.id, typeOfExamination, httpOptions);
  }

  deleteTypeOfExamination(id: number) {
    return this.httpClient.delete(baseUrl + 'types-of-examination/' + id, httpOptions);
  }

  getDoctorsBySpecialiyation(id: number) {
    return this.httpClient.get<User[]>(baseUrl + 'types-of-examination/specialization/' + id, httpOptions);
  }

  getTypeOfExamination(id: number) {
    return this.httpClient.get<TypeOfExamination>(baseUrl + 'types-of-examination/' + id, httpOptions);
  }
 
}