import { Component, OnInit, HostListener, ChangeDetectorRef, ViewChild } from '@angular/core';
import { ClinicService } from 'src/app/service/clinic.service';
import { User } from 'src/app/model/user';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.scss']
})
export class PatientsComponent implements OnInit {
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective
  
  patients: User[] = [];
  previous: any = [];
  searchText: string = '';
  headElements = ['ID', 'Username', 'Name', 'Last name', 'E-mail', 'Gender', 'Date of birth', ''];
  headElementsModel = ['id', 'username', 'name', 'lastName', 'email', 'gender', 'dateOfBirth'];

  constructor(private clinicService: ClinicService, private cdRef: ChangeDetectorRef, private datePipe: DatePipe) { }

  @HostListener('input') oninput() { this.searchItems(); }

  ngOnInit() {
    this.clinicService.getClinicPatients().subscribe(res => {
      this.patients = res; 
      this.mdbTable.setDataSource(this.patients);
      this.patients= this.mdbTable.getDataSource();
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
      this.patients = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.patients =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['username','name', 'lastName']); 
      this.mdbTable.setDataSource(prev); 
    }
  }
}
