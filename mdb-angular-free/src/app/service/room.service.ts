import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions } from './constants';
import { Observable } from 'rxjs';
import { Room } from 'src/app/model/room';
@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(private httpClient: HttpClient) { }

  getClinicRooms():Observable<Room[]> {
    return this.httpClient.get<Room[]>(baseUrl + 'rooms', httpOptions);
  }
}
