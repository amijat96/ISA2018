import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';

@Component({
  selector: 'app-medical-staff',
  templateUrl: './medical-staff.component.html',
  styleUrls: ['./medical-staff.component.scss']
})
export class MedicalStaffComponent implements OnInit, AfterViewInit {
  
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective

  headElements = ['ID', 'Name', 'Last Name', 'Username', 'E-mail', 'Grade', 'Commands'];
  headElementsModel = ['userId', 'name', 'lastName', 'username', 'email', 'doctorGrade'];
  medicalStaff: User[] = [];
  previous: any = [];
  searchText: string = '';

  constructor(private userService: UserService, private cdRef: ChangeDetectorRef) { }

  ngOnInit() {
    this.userService.getMedicalStaff().subscribe(res => {
      this.medicalStaff = res;
      this.mdbTable.setDataSource(this.medicalStaff);
      this.medicalStaff= this.mdbTable.getDataSource();
      this.previous = this.mdbTable.getDataSource();
    });
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
      this.medicalStaff = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.medicalStaff =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['name', 'lastName', 'username']); 
      this.mdbTable.setDataSource(prev); 
    }
  }
}
