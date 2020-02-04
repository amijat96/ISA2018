import { Component, OnInit, ViewChild, HostListener, ChangeDetectorRef, AfterViewInit } from '@angular/core';
import { ScheduleService } from 'src/app/service/schedule.service';
import { SchedulesByDate } from 'src/app/model/schedulesByDate';
import { Schedule } from 'src/app/model/schedule';
import { MdbTablePaginationComponent, MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { DatePipe } from '@angular/common';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.scss']
})
export class ScheduleComponent implements OnInit, AfterViewInit {
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  @ViewChild('newScheduleModal', {static: false}) newScheduleModal: ModalDirective;
  @ViewChild('editScheduleModal', {static: false}) editScheduleModal: ModalDirective;
  @ViewChild('confirmDeleteScheduleModal', {static: false}) confirmDeleteScheduleModal: ModalDirective;
  
  headElements = ['ID', 'Username', 'First Name', 'Last Name', 'From Date', 'To Date', 'Shift time', ''];
  headElementsModel = ['scheduleId', 'doctorUsername', 'doctorName', 'doctorLastName', 'startDate', 'endDate'];

  fromDate: Date;
  toDate: Date;
  schedules: Schedule[] = [];
  previous: any = [];
  searchText: string = '';
  medicalStaff: User[] = [];
  validatingForm: FormGroup;
  validatingEditForm: FormGroup;
  schedule:Schedule = new Schedule();
  buttonDisabled = false;

  constructor(private scheduleService: ScheduleService,
              private cdRef: ChangeDetectorRef, 
              public datepipe: DatePipe,
              public userService: UserService
              ) {
                this.validatingForm = new FormGroup({
                  startDateControl: new FormControl(null, [ Validators.required]),
                  endDateControl: new FormControl(null, [ Validators.required]),
                  startTimeControl: new FormControl(null,[ Validators.required]),
                  endTimeControl: new FormControl(null,[ Validators.required])
                })
            
                this.validatingEditForm = new FormGroup({
                  editStartDateControl: new FormControl(null, [ Validators.required]),
                  editEndDateControl: new FormControl(null, [ Validators.required]),
                  editStartTimeControl: new FormControl(null,[ Validators.required]),
                  editEndTimeControl: new FormControl(null,[ Validators.required])
                })
               }

  get startDateInput() { return this.validatingForm.get('startDateControl'); }
  get endDateInput() { return this.validatingForm.get('endDateControl'); }
  get startTimeInput() { return this.validatingEditForm.get('startTimeControl'); }
  get endTimeInput() { return this.validatingEditForm.get('endTimeControl'); }

  get editStartDateInput() { return this.validatingForm.get('editStartDateControl'); }
  get editEndDateInput() { return this.validatingForm.get('editEndDateControl'); }
  get editStartTimeInput() { return this.validatingEditForm.get('editStartTimeControl'); }
  get editEndTimeInput() { return this.validatingEditForm.get('editEndTimeControl'); }

  ngOnInit() {
    this.userService.getMedicalStaff().subscribe(res => this.medicalStaff = res);
  }

  ngAfterViewInit() {
    this.mdbTablePagination.setMaxVisibleItemsNumberTo(5);
    this.mdbTablePagination.calculateFirstItemIndex();
    this.mdbTablePagination.calculateLastItemIndex();
    this.cdRef.detectChanges();
  }

  @HostListener('input') oninput() { this.searchItems(); }

  getSchedules() {
    if(this.fromDate != null && this.toDate != null) {
      var scheduleRequest = new SchedulesByDate();
      scheduleRequest.startDate = this.datepipe.transform(this.fromDate, 'yyyy-MM-dd');
      scheduleRequest.endDate =  this.datepipe.transform(this.toDate, 'yyyy-MM-dd');
      this.scheduleService.getSchedules(scheduleRequest).subscribe(
        (data: Schedule[]) => {
          this.schedules = data;
          this.mdbTable.setDataSource(data);
          this.schedules= this.mdbTable.getDataSource();
          this.previous = this.mdbTable.getDataSource();
        }
      )
      this.schedule = new Schedule();
    }
  }

  searchItems() { 
    const prev = this.mdbTable.getDataSource();
    if (!this.searchText) {
      this.mdbTable.setDataSource(this.previous); 
      this.schedules = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.schedules = this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['userUsername', 'typeName', 'doctorUsername']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  createScheduleModal() {
    this.newScheduleModal.show();
  }

  createSchedule() {
    this.schedule.startDate = this.datepipe.transform(this.schedule.startDate, 'yyyy-MM-dd');
    this.schedule.endDate = this.datepipe.transform(this.schedule.endDate, 'yyyy-MM-dd');
    this.scheduleService.createSchedule(this.schedule).subscribe(res => {
      this.newScheduleModal.hide();
      this.getSchedules();
    });
  }
  updateScheduleModal(schedule: Schedule) {
    this.schedule = schedule;
    this.editScheduleModal.show();
  }

  updateSchedule() {
    this.schedule.startDate = this.datepipe.transform(this.schedule.startDate, 'yyyy-MM-dd');
    this.schedule.endDate = this.datepipe.transform(this.schedule.endDate, 'yyyy-MM-dd');
    this.scheduleService.updateSchedule(this.schedule).subscribe(res => { 
      this.editScheduleModal.hide();
      this.getSchedules();
    });
  }

  deleteScheduleModal(schedule: Schedule) {
    this.schedule = schedule;
    this.confirmDeleteScheduleModal.show();
  }

  deleteSchedule() {
    this.scheduleService.deleteSchedule(this.schedule.scheduleId).subscribe(res => { 
      this.confirmDeleteScheduleModal.hide();
      this.getSchedules();
    })
  }

  OnDateTimeChange($event) {
    if (this.schedule.startDate >= this.schedule.endDate) this.buttonDisabled = true;
    else this.buttonDisabled = false;
  }
}
