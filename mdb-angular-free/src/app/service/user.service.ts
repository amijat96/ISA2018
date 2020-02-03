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

  getMyAccount() {
    return this.httpClient.get<User>(baseUrl + 'users/me', httpOptions);
  }

  updateUser(user: User) {
    return this.httpClient.put<User>(baseUrl + 'users/' + user.id, user, httpOptions);
  }

  changePassword(password: string) {
    return this.httpClient.put(baseUrl + 'users/change-password/' + password, httpOptions);
  }

  getUserByUsername(username: string) {
    return this.httpClient.get<User>(baseUrl + 'users/' + username, httpOptions);
  }
}

