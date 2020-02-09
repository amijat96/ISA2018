import { Component, OnInit, AfterViewInit, ViewChild, ChangeDetectorRef, HostListener } from '@angular/core';
import { MdbTablePaginationComponent, MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { Vacation } from 'src/app/model/vacation';
import { VacationService } from 'src/app/service/vacation.service';
import { DatePipe } from '@angular/common';
import { VacationsByDate } from 'src/app/model/vacationsByDate';

@Component({
  selector: 'app-vacation-requests',
  templateUrl: './vacation-requests.component.html',
  styleUrls: ['./vacation-requests.component.scss']
})
export class VacationRequestsComponent implements OnInit, AfterViewInit {
    @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
    @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
    @ViewChild('approveModal', {static: false}) approveModal: ModalDirective;
    @ViewChild('denyModal', {static: false}) denyModal: ModalDirective;


    vacations: Vacation[] = [];
    searchText: string = '';
    previous: any[] = [];
    headElements = ['ID', 'Username', 'From Date', 'To Date', 'Accepted', 'Description', ''];
    headElementsModel = ['vacationId', 'userUsername', 'startDate', 'accepted', 'description'];
    fromDate: Date;
    toDate: Date;
    description;
    vacation: Vacation;

    constructor(public vacationService: VacationService,
      private cdRef: ChangeDetectorRef,
      public datepipe: DatePipe, 
      ) { }
  
    ngOnInit() {
    }
  
    ngAfterViewInit() {
      this.mdbTablePagination.setMaxVisibleItemsNumberTo(5);
      this.mdbTablePagination.calculateFirstItemIndex();
      this.mdbTablePagination.calculateLastItemIndex();
      this.cdRef.detectChanges();
    }
  
    @HostListener('input') oninput() { this.searchItems(); }
  
    searchItems() { 
      const prev = this.mdbTable.getDataSource();
      if (!this.searchText) {
        this.mdbTable.setDataSource(this.previous); 
        this.vacations = this.mdbTable.getDataSource(); 
      } 
      if (this.searchText) { 
        this.vacations = this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['userUsername']); 
        this.mdbTable.setDataSource(prev); 
      }
    }
  
    getVacations() {
      if(this.fromDate != null && this.toDate != null) {
        this.vacations = [];
        var vacationRequest = new VacationsByDate();
        vacationRequest.startDate = this.datepipe.transform(this.fromDate, 'yyyy-MM-dd');
        vacationRequest.endDate =  this.datepipe.transform(this.toDate, 'yyyy-MM-dd');
        this.vacationService.getVacations(vacationRequest).subscribe(
          (data: Vacation[]) => {
            data.forEach(vacation => {
              if(!vacation.accepted && vacation.description == null) {this.vacations.push(vacation);}
            })
            this.mdbTable.setDataSource(this.vacations);
            this.vacations= this.mdbTable.getDataSource();
            this.previous = this.mdbTable.getDataSource();
          }
        )
      }
    }

    approveVacationModal(vacation: Vacation) {
      this.vacation = vacation;
      this.approveModal.show();
    }

    approveVacation() {
      this.vacationService.approveVacation(this.vacation).subscribe(
        (data: Vacation) => { 
          this.approveModal.hide();
          this.getVacations();
        }
      )
    }

    denyVacationModal(vacation: Vacation) {
      this.vacation = vacation;
      this.denyModal.show();
    }

    denyVacation() {
      this.vacation.description = this.description;
      this.vacationService.denyVacation(this.vacation).subscribe(
        (data: Vacation) => {
          this.denyModal.hide();
          this.getVacations();
        }
      )
    }
  }
