import { Component, OnInit, AfterViewInit, ViewChild, ChangeDetectorRef, HostListener } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { VacationService } from 'src/app/service/vacation.service';
import { User } from 'src/app/model/user';
import { Vacation } from 'src/app/model/vacation';
import { MdbTablePaginationComponent, MdbTableDirective, ModalDirective } from 'angular-bootstrap-md';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-doctor-vacations',
  templateUrl: './doctor-vacations.component.html',
  styleUrls: ['./doctor-vacations.component.scss']
})
export class DoctorVacationsComponent implements OnInit, AfterViewInit {
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  @ViewChild('newVacation', {static: false}) newVacation: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;

  user: User = new User();
  vacations: Vacation[] = [];
  searchText: string = '';
  previous: any[] = [];
  headElements = ['ID', 'From Date', 'To Date', 'Accepted', 'Description'];
  headElementsModel = ['vacationId', 'startDate', 'accepted', 'description'];
  fromDate: Date;
  toDate: Date;
  message:string = '';

  constructor(
    private userService: UserService,
    private vacationsService: VacationService,
    private cdRef: ChangeDetectorRef,
    public datePipe: DatePipe, 
  ) {}

  ngOnInit() {
    this.userService.getMyAccount().subscribe( res => {
      this.user = res;
      this.vacationsService.getDoctorVacations(this.user.username).subscribe(data => {
        this.vacations = data;
        this.mdbTable.setDataSource(this.vacations);
        this.vacations= this.mdbTable.getDataSource();
        this.previous = this.mdbTable.getDataSource();
      })
    })
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

  newVacationModal() {
    this.newVacation.show();
  }
  createVacationRequest() {
    var request = new Vacation();
    request.startDate =  this.datePipe.transform(this.fromDate, 'yyyy-MM-dd');
    request.endDate =  this.datePipe.transform(this.toDate, 'yyyy-MM-dd')
    request.userId = this.user.id;
    request.description = null;
    console.log(request);
    this.vacationsService.createVacation(request).subscribe(
      (data: Vacation) => {
        this.successModal.show();
        this.ngOnInit();
        console.log("kreirano");
      },
      error => { 
        this.message = error.error.message;
        this.errorModal.show();
        console.log(this.message);
      }    
    )
  } 
}
