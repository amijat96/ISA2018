import { Component, OnInit, HostListener, ViewChild } from '@angular/core';
import { ExaminationService } from 'src/app/service/examination.service';
import { UserService } from 'src/app/service/user.service';
import { Examination } from 'src/app/model/examination';
import { User } from 'src/app/model/user';
import { DatePipe} from '@angular/common';
import { TypeOfExaminationService } from 'src/app/service/type-of-examination.service';
import { Room } from 'src/app/model/room';
import { RoomService } from 'src/app/service/room.service';
import { MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { FreeTermsRequest } from 'src/app/model/freeTermsRequest';
import { TypeOfExamination } from 'src/app/model/typeOfExamination';
import { FreeTerm } from 'src/app/model/roomFreeTerms';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-examination',
  templateUrl: './examination.component.html',
  styleUrls: ['./examination.component.scss']
})

export class ExaminationComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;

  examination = new Examination;
  patient = new User;
  doctor = new User;
  requestedDateTime:string = '';
  doctorsBySpecialization: User[] = []; 
  rooms: Room[] = [];
  headElements = ['ID', 'Floor', 'Number', 'Type', ''];
  headElementsModel = ['roomId', 'floor', 'number', 'roomType'];
  freeTermsHeadElements = ['From dateTime', 'To dateTime', ''];
  freeTermsHeadElementsModel = ['startDateTime', 'endDateTime'];
  searchText: string = '';
  previous: any = [];
  selectedRoom = new Room();
  typeOfExamination = new TypeOfExamination();
  examinationRoomTypeId;
  freeTerms: FreeTerm[] = [];
  roomFreeTerms: FreeTerm[] = [];
  termStart = '';
  termEnd = '';
  selectedTerm: FreeTerm;
  disableDiscount = true;
  dateTimeHidden = true;
  errorMessage: string = '';
  validatingForm: FormGroup;
  examinationTime: string;
  examinationDate: Date;
  disableConfirmButton = true;

  constructor(
    private examinationService: ExaminationService, 
    private userService: UserService, 
    private typeOfExaminationService: TypeOfExaminationService, 
    private roomService: RoomService,
    private datePipe: DatePipe
    ) {
    this.validatingForm = new FormGroup({
      dateControl: new FormControl(null, [ Validators.required]),
      timeControl: new FormControl(null,[Validators.required]),
    }) 
  }

  get dateInput() { return this.validatingForm.get('dateInput'); }
  get timeInput() { return this.validatingForm.get('timeInput'); }
  
  ngOnInit() {
    if(window.location.href.substring(49, 11) != '/predefined') {
      this.examinationService.getExamination(Number(window.location.href.substring(49, window.location.href.length))).subscribe(
        (data: Examination) => {
          this.examination = data;
          this.requestedDateTime = this.datePipe.transform(data.dateTime,"MMM d, y, HH:mm:ss");
          this.userService.getUserByUsername(data.userUsername).subscribe(res => this.patient = res);
          this.userService.getUserByUsername(data.doctorUsername).subscribe(res => this.doctor = res);
          this.typeOfExaminationService.getDoctorsBySpecialiyation(data.typeId).subscribe(res => { this.doctorsBySpecialization = res});
          this.roomService.getClinicRooms().subscribe(res => {
            res.forEach(room => 
            {
              if(room.roomType == data.roomType) {
                this.rooms.push(room);
              }
            })
          });

          var request = new FreeTermsRequest();
          request.doctorId = data.doctorId;
          if(data.roomType == 'EXAMINATION') request.roomTypeId = 1;
          else request.roomTypeId = 2;
          request.dateTime = data.dateTime;
          request.duration = data.duration;
          this.roomService.getFreeTerms(request).subscribe(res => this.freeTerms = res);

          this.mdbTable.setDataSource(this.rooms);
          this.rooms= this.mdbTable.getDataSource();
          this.previous = this.mdbTable.getDataSource();
          this.disableDiscount = true;
        } 
      )
      
    }
    else {
      console.log('predefined');
    }
  }

  @HostListener('input') oninput() { this.searchItems(); }

  searchItems() { 
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous); 
      this.rooms = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.rooms =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['number']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  onDoctorChanged(event) {
    this.userService.getUserByUsername(this.examination.doctorUsername).subscribe(res => { 
      this.doctor = res;
      var request = new FreeTermsRequest();
      request.doctorId = res.id;
      if(this.examination.roomType == 'EXAMINATION') request.roomTypeId = 1;
      else request.roomTypeId = 2;
      request.dateTime = this.examination.dateTime;
      request.duration = this.examination.duration;
      this.freeTerms = [];
      this.roomService.getFreeTerms(request).subscribe((res: FreeTerm[]) => this.freeTerms = res,
        error => { this.errorMessage = error.error.message;  this.errorModal.show()}
        );
    });
    this.selectedRoom = new Room();
    this.roomFreeTerms = [];
    this.roomFreeTerms = [];
    this.dateTimeHidden = true;
    this.termStart = '';
    this.termEnd = '';
  }

  getRoomFreeTerms(room :Room) {
    this.roomFreeTerms = [];
    this.selectedRoom = room;
    this.freeTerms.forEach(
      (term: FreeTerm) => { if(term.roomId == room.roomId) this.roomFreeTerms.push(term) });
  }

  selectTerm(term: FreeTerm) {
    this.termStart = this.datePipe.transform(term.startDateTime,"MMM d, y, HH:mm:ss");
    this.termEnd = this.datePipe.transform(term.endDateTime,"MMM d, y, HH:mm:ss");
    this.dateTimeHidden = false;
    this.selectedTerm = term;
  }

  OnDateTimeChange(event) {
    if(this.examinationTime != undefined && this.examinationDate != undefined) {
      this.examinationDate.setHours(Number(this.examinationTime.substr(0,2)), Number(this.examinationTime.substr(3,2)), 0);

      var examDate = new Date(this.datePipe.transform(this.examinationDate,"MMM d, y, HH:mm:ss"))
      var startDate = new Date(this.datePipe.transform(this.selectedTerm.startDateTime,"MMM d, y, HH:mm:ss"))
      var endDate = new Date(this.datePipe.transform(this.selectedTerm.endDateTime,"MMM d, y, HH:mm:ss"))

      if( examDate < startDate || examDate > endDate) { console.log('ne valjaa'); this.disableConfirmButton = true; }
      else { this.disableConfirmButton = false; console.log('valjaaa');}
    }
    else this.disableConfirmButton = true;
  }

  approveExamination() {
    console.log(this.examination);
    this.examination.dateTime = new Date(this.datePipe.transform(this.examinationDate,"MMM d, y, HH:mm:ss"));
    this.examination.roomId = this.selectedRoom.roomId;
    this.examination.doctorId = this.doctor.id;
    this.examinationService.approveExamination(this.examination).subscribe(
      (data: Examination) => {
        this.successModal.show();
      },
      error => {
        this.errorMessage = error.error.message;  
        this.errorModal.show();
      }
    )
  }
}
