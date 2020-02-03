import { Component, OnInit, ViewChild, AfterViewInit, HostListener, ChangeDetectorRef } from '@angular/core';
import { ClinicService } from 'src/app/service/clinic.service';
import { Examination } from 'src/app/model/examination';
import { MdbTableDirective, MdbTablePaginationComponent } from 'angular-bootstrap-md';

@Component({
  selector: 'app-examinations',
  templateUrl: './examinations.component.html',
  styleUrls: ['./examinations.component.scss']
})
export class ExaminationsComponent implements OnInit, AfterViewInit{
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective;
  
  headElements = [];
  headElementsModel = ['examinationId', 'userUsername', 'dateTime', 'typeName', 'doctorUsername', 'roomType', 'roomFloor', 'roomNumber', 'price', 'dicount', 'predefined', 'accepted'];
  examinations: Examination[] = [];
  confirmedExaminations: Examination[] = [];
  requestedExaminations: Examination[] = [];
  previous: any = [];
  searchText: string = '';
  buttonDisabled = false;

  constructor(private clinicService: ClinicService, private cdRef: ChangeDetectorRef) { }

  @HostListener('input') oninput() { this.searchItems(); }

  ngOnInit() {
    this.clinicService.getClinicExaminations().subscribe(res => {
      res.forEach(examination => {
        if(examination.roomId != null && examination.roomId != 0) this.confirmedExaminations.push(examination);
        else this.requestedExaminations.push(examination);
      });
      this.setExaminations(); 
      this.mdbTable.setDataSource(this.examinations);
      this.examinations= this.mdbTable.getDataSource();
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
      this.examinations = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.examinations = this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['userUsername', 'typeName', 'doctorUsername']); 
      this.mdbTable.setDataSource(prev); 
    }
  }


  setExaminations() {
    if(window.location.href == 'http://localhost:4200/admin-profile/examinations') { 
      this.examinations = this.confirmedExaminations; 
      this.buttonDisabled = true;
      this.headElements = ['ID', 'Patient', 'Date & time', 'Type', 'Doctor', 'E/O', 'Room floor', 'Room number', 'Price(€)', 'Discount(%)', 'Predefined', 'Accepted'];
    }
    else {
      this.examinations = this.requestedExaminations; 
      this.buttonDisabled = false; 
      this.headElements = ['ID', 'Patient', 'Date & time', 'Type', 'Doctor', 'E/O', '']
    } 
  }
  
}


