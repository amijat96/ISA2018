import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { RoomService } from 'src/app/service/room.service';
import { Room } from 'src/app/model/room';
import { RoomType } from 'src/app/model/roomType';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-rooms',
  templateUrl: './rooms.component.html',
  styleUrls: ['./rooms.component.scss']
})
export class RoomsComponent implements OnInit, AfterViewInit{

  @ViewChild('newRoomModal', {static: false}) newRoomModal: ModalDirective;
  @ViewChild('editRoomModal', {static: false}) editRoomModal: ModalDirective;
  @ViewChild('confirmDeleteRoomModal', {static: false}) confirmDeleteRoomModal: ModalDirective;
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
  roomEdit = new Room();
  errorMessage: string = "";
  validatingForm: FormGroup;
  validatingEditForm: FormGroup;
  deleteRoomId: number;


  constructor(private roomService: RoomService, private cdRef: ChangeDetectorRef) {
    this.validatingForm = new FormGroup({
      floorControl: new FormControl(null, [ Validators.required, Validators.min(0), Validators.max(999)]),
      numberControl: new FormControl(null,[ Validators.required, Validators.maxLength(10)])
    })

    this.validatingEditForm = new FormGroup({
      editFloorControl: new FormControl(null, [ Validators.required, Validators.min(0), Validators.max(999)]),
      editNumberControl: new FormControl(null,[ Validators.required, Validators.maxLength(10)])
    })
  }

  get floorInput() { return this.validatingForm.get('floorControl'); }
  get numberInput() { return this.validatingForm.get('numberControl'); }
  get editFloorInput() { return this.validatingEditForm.get('editFloorControl'); }
  get editNumberInput() { return this.validatingEditForm.get('editNumberControl'); }

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

  //action for showing modal for creating room
  createRoomModal() {
    this.newRoomModal.show();
    this.roomService.getRoomTypes().subscribe(res => this.roomTypes = res);
  }

  createRoom() {
    this.roomService.createRoom(this.room).subscribe(
      (data: Room) => { this.newRoomModal.hide();
                        this.ngOnInit(); },
      error => {this.errorMessage = error.error.message;  console.log(this.errorMessage); }
    );
   
  }

  //action for showing modal for editing room
  updateRoomModal(room1: Room) {
    this.roomEdit = room1;
    this.roomService.getRoomTypes().subscribe(res => {
      this.roomTypes = res;
      this.roomTypes.forEach(type => {
        if(type.name == room1.roomType) this.roomEdit.roomTypeId = type.roomTypeId;
      });
      this.editRoomModal.show();
    });
  }

  updateRoom() {
    this.roomService.updateRoom(this.roomEdit).subscribe(
      (data: Room) => { this.editRoomModal.hide();
                        window.location.reload(); },
      error => { this.errorMessage = error.error.message;  console.log(this.errorMessage); }
    )
   
  }

  deleteRoomModal(roomId: number) {
    this.deleteRoomId = roomId;
    this.confirmDeleteRoomModal.show();
  }

  deleteRoom() {
    this.roomService.deleteRoom(this.deleteRoomId).subscribe(
      (data: boolean) => { this.confirmDeleteRoomModal.hide();
        this.ngOnInit();}
    )
    
  }
}
