import { Component, OnInit, ViewChild, HostListener, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/model/user';
import { MedicalRecord } from 'src/app/model/medical-record';
import { Examination } from 'src/app/model/examination';
import { DatePipe } from '@angular/common';
import { MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { ReportService } from 'src/app/service/report.service';
import { Report } from 'src/app/model/report';

@Component({
  selector: 'app-patient',
  templateUrl: './patient.component.html',
  styleUrls: ['./patient.component.scss']
})
export class PatientComponent implements OnInit {
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective
  @ViewChild('reportInfoModal', {static: false}) reportInfoModal: ModalDirective;

  previous: any[] = [];
  currentDoctor: User = new User();
  username: string = '';
  user: User = new User();
  picture: string = '';
  medicalRecordTab = true;
  medicalRecord: MedicalRecord = new MedicalRecord();
  examinations: Examination[] = [];
  headElements = ['Doctor', 'Name', 'Type', 'Date & time', '']
  headElementsModel = ['doctorUsername', 'typeName', 'roomType', 'dateTime'];
  searchText: string = '';
  report: Report = new Report();

  constructor(
    private route: ActivatedRoute, 
    private userService: UserService, 
    private datePipe: DatePipe, 
    private cdRef: ChangeDetectorRef,
    private reportService: ReportService
    ) { 
    this.username = this.route.snapshot.params['username'];
    this.userService.getMyAccount().subscribe(res => this.currentDoctor = res);
  }

  @HostListener('input') oninput() { this.searchItems(); }

  ngOnInit() {
    this.userService.getUserByUsername(this.username).subscribe(user => {
      this.user = user;
      this.setPicture(user);
      this.userService.getMedicalRecord(user.username).subscribe(record => this.medicalRecord = record);
      this.userService.getExaminations(user.username).subscribe(examinations => {
        examinations.forEach(exam => {
          if(exam.roomId != 0 && exam.accepted) this.examinations.push(exam);
        })
        this.mdbTable.setDataSource(this.examinations);
        this.examinations= this.mdbTable.getDataSource();
        this.previous = this.mdbTable.getDataSource(); 
      });
    })
  }

  searchItems() { 
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous); 
      this.examinations = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.examinations =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['doctorUsername', 'typeName']); 
      this.mdbTable.setDataSource(prev); 
    }
  }
  setPicture(user: User) {
    if(user.gender != 1) this.picture = '/assets/images/patient_male_avatar.jpg';
    else this.picture =  '/assets/images/patient_female_avatar.png';
  }

  setMedicalRecordTab(tab: boolean) {
    this.medicalRecordTab = tab;
  }

  getInfo(reportId: number) {
    this.reportService.getReport(reportId).subscribe(res => {
      this.report = res;
      console.log(res);
      this.reportInfoModal.show();
    });
  }
}
