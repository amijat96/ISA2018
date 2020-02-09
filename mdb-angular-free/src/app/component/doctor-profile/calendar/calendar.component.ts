import { Component, OnInit, ChangeDetectorRef, HostListener, ViewChild } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { DatePipe } from '@angular/common';
import { User } from 'src/app/model/user';
import { Examination } from 'src/app/model/examination';
import { MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { ExaminationService } from 'src/app/service/examination.service';

@Component({
  selector: 'app-calendar',
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit {
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;

  searchText: string = '';
  date = new Date();
  doctor: User = new User();
  examinations: Examination[] = [];
  headElements = ['ID', 'Patient', 'Date & time', 'Name',  'E/O', 'Room floor', 'Room number', ''];
  headElementsModel = ['examinationId', 'userUsername', 'dateTime', 'typeName', 'roomType', 'roomFloor', 'roomNumber'];
  previous: any = [];

  constructor(
    private userService: UserService, 
    private datePipe: DatePipe, 
    private cdRef: ChangeDetectorRef,
    private examinationService: ExaminationService
    ) { }

  @HostListener('input') oninput() { this.searchItems(); }

  ngOnInit() {
    this.userService.getMyAccount().subscribe( res => { 
      this.doctor = res;
      this.getExaminations();
    });
  }

  nextDay() {
    this.date.setDate(this.date.getDate() + 1);
    this.date = new Date(this.date);
    this.getExaminations();
  }

  previousDay() {
    this.date.setDate(this.date.getDate() - 1);
    this.date = new Date(this.date);
    this.getExaminations();
  }

  getExaminations() {
    this.examinations = [];
    this.userService.getDoctorExaminationsByDate(this.doctor.username, this.datePipe.transform(this.date, 'yyyy-MM-dd')).subscribe(res => {
      this.examinations = res;
      this.mdbTable.setDataSource(this.examinations);
      this.examinations= this.mdbTable.getDataSource();
      this.previous = this.mdbTable.getDataSource();
    });
  }

  searchItems() { 
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous); 
      this.examinations = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.examinations = this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['userUsername', 'typeName']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  createReport(examination: Examination) {
    window.location.href = "http://localhost:4200/doctor-profile/patients/" + examination.userUsername + "/examination/" + examination.examinationId + "/report";
  }

  cancelExamination(examination: Examination) {
    this.examinationService.cancelExamination(examination.examinationId).subscribe(
      (data: Examination) => { 
        this.successModal.show();
        this.getExaminations();
      },
      error => this.errorModal.show()
    )
  }
}

