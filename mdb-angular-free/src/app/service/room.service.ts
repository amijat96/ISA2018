import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { baseUrl, httpOptions} from './constants';
import { Observable } from 'rxjs';
import { Room } from 'src/app/model/room';
import { RoomType } from '../model/roomType';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(private httpClient: HttpClient) { }

  getClinicRooms():Observable<Room[]> {
    return this.httpClient.get<Room[]>(baseUrl + 'rooms', httpOptions);
  }

  getRoomTypes():Observable<RoomType[]> {
    return this.httpClient.get<RoomType[]>(baseUrl + 'roomTypes', httpOptions);
  }

  createRoom(room: Room) {
    room.clinicId = Number(localStorage.getItem('clinicId'));
    return this.httpClient.post(baseUrl +'rooms' , room, httpOptions);
  }

  updateRoom(room: Room) {
    room.clinicId = Number(localStorage.getItem('clinicId'));
    return this.httpClient.put(baseUrl + 'rooms/' + room.roomId, room, httpOptions);
  }

  deleteRoom(roomId: number) {
    return this.httpClient.delete(baseUrl + 'rooms/' + roomId, httpOptions);
  }
}
