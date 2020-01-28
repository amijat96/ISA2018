import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { User } from 'src/app/model/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  getMedicalStaff():Observable<User[]> {
    return this.httpClient.get<User[]>(baseUrl + 'users/' + localStorage.getItem('clinicId') + '/medical-staff', httpOptions);
  }

  deleteUser(id: number) {
    console.log(baseUrl + 'users/' + id);
    return this.httpClient.delete(baseUrl + 'users/' + id, httpOptions);
  }

}
