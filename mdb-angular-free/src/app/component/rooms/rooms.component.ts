import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { RoomService } from 'src/app/service/room.service';
import { Room } from 'src/app/model/room';
import { RoomType } from 'src/app/model/roomType';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';
import { ModalDirective } from 'angular-bootstrap-md';
import { RoomRequest } from 'src/app/model/roomRequest';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.scss']
})
export class RoomsComponent implements OnInit, AfterViewInit{

  @ViewChild('newRoomModal', {static: false}) newRoomModal: ModalDirective;
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective

  rooms: Room[] = [];
  elements: Room[] = [];
  previous: any = [];
  headElements = ['ID', 'Floor', 'Number', 'Type', 'Commands'];
  headElementsModel = ['roomId', 'floor', 'number', 'roomType'];
  searchText: string = '';
  roomTypes: RoomType[];
  room = new Room();
  errorMessage: string = "";

  constructor(private roomService: RoomService, private cdRef: ChangeDetectorRef) {}

  @HostListener('input') oninput() { this.searchItems(); }

  ngOnInit() {
    this.roomService.getClinicRooms().subscribe(res => {
      this.elements = res; 
      this.mdbTable.setDataSource(this.elements);
      this.elements= this.mdbTable.getDataSource();
      this.previous = this.mdbTable.getDataSource();
    });      
  }

  ngAfterViewInit() {
    this.mdbTablePagination.setMaxVisibleItemsNumberTo(5);
    this.mdbTablePagination.calculateFirstItemIndex();
    this.mdbTablePagination.calculateLastItemIndex();
    this.cdRef.detectChanges();
  }

  searchItems() { 
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous); 
      this.elements = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.elements =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['number','roomType']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  newRoom() {
    this.newRoomModal.show();
    this.roomService.getRoomTypes().subscribe(res => this.roomTypes = res);
  }

  createRoom() {
    this.roomService.createRoom(this.room).subscribe(
      (data: RoomRequest) => console.log('success'),
      error => {this.errorMessage = error.error.message;  console.log(this.errorMessage); }
    );
    this.newRoomModal.hide();
    window.location.reload();
  }
}
