import { Component, OnInit } from '@angular/core';
import { RoomService } from 'src/app/service/room.service';
import { observable, Observable } from 'rxjs';
import { Room } from 'src/app/model/room';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.scss']
})
export class RoomsComponent implements OnInit {

  headElements = ['ID', 'Floor', 'Number', 'Type'];
  rooms: Room[];

  constructor(private roomService: RoomService) { }

  ngOnInit() {
    this.roomService.getClinicRooms().subscribe(res => this.rooms = res);
  }

}
