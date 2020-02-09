import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef, HostListener } from '@angular/core';
import { TypeOfExaminationService } from 'src/app/service/type-of-examination.service';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';
import { TypeOfExamination } from 'src/app/model/typeOfExamination';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, Validators, FormControl} from '@angular/forms';
import { RoomType } from 'src/app/model/roomType';
import { RoomService } from 'src/app/service/room.service';

@Component({
  selector: 'app-types-of-examination',
  templateUrl: './types-of-examination.component.html',
  styleUrls: ['./types-of-examination.component.scss']
})
export class TypesOfExaminationComponent implements OnInit, AfterViewInit{

  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective
  @ViewChild('newTypeOfExaminationModal', {static: false}) newTypeOfExaminationModal: ModalDirective;
  @ViewChild('editTypeOfExaminationModal', {static: false}) editTypeOfExaminationModal: ModalDirective;
  @ViewChild('deleteTypeOfExaminationModal', {static: false}) deleteTypeOfExaminationModal: ModalDirective;

  headElements = ['ID', 'Name', 'Duration', 'Description', 'Type', 'Commands'];
  headElementsModel = ['id', 'name', 'duration', 'description', 'typeName'];
  typesOfExamination: TypeOfExamination[] = [];
  previous: any = [];
  searchText: string = '';
  validatingForm: FormGroup;
  validatingEditForm: FormGroup;
  typeOfExamination = new TypeOfExamination();
  typeOfExaminationEdit = new TypeOfExamination();
  roomTypes: RoomType[];
  errorMessage: string = "";
  typeOfExaminationId: number = 0;

  constructor(private typesOfExaminationService: TypeOfExaminationService, private roomService: RoomService, private cdRef: ChangeDetectorRef) {
    this.validatingForm = new FormGroup({
      nameControl: new FormControl(null, [ Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      descriptionControl: new FormControl(null,[Validators.maxLength(200)]),
      durationControl: new FormControl(null,[ Validators.required])
    })

    this.validatingEditForm = new FormGroup({
      editNameControl: new FormControl(null, [Validators.required, Validators.min(2), Validators.max(40)]),
      editDescriptionControl: new FormControl(null,[Validators.maxLength(200)]),
      editDurationControl: new FormControl(null,[ Validators.required])
    })
  }

  get nameInput() { return this.validatingForm.get('nameControl'); }
  get descriptionInput() { return this.validatingForm.get('descriptionControl'); }
  get editNameInput() { return this.validatingEditForm.get('editNameControl'); }
  get editDescriptionInput() { return this.validatingEditForm.get('editDescriptionControl'); }

  ngOnInit() {
    this.typesOfExaminationService.getTypesOfExamination().subscribe(res => {
      this.typesOfExamination = res; 
      this.mdbTable.setDataSource(this.typesOfExamination);
      this.typesOfExamination= this.mdbTable.getDataSource();
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
      this.typesOfExamination = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.typesOfExamination =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['name','typeName']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  createTypeOfExaminationModal() {
      this.newTypeOfExaminationModal.show();
      this.roomService.getRoomTypes().subscribe(res => this.roomTypes = res);
  }

  createTypeOfExamination() {
    console.log(this.typeOfExamination.name);
    this.typesOfExaminationService.createTypeOfExamination(this.typeOfExamination).subscribe(
      (data: TypeOfExamination) =>{
        this.ngOnInit();
        this.newTypeOfExaminationModal.hide();
      },
      error => {this.errorMessage = error.error.message;  console.log(this.errorMessage); 
    });
    
  }

  updateTypeOfExaminationModal(id: number, typeOfExamination1: TypeOfExamination) {
    this.typeOfExaminationEdit = typeOfExamination1;
    this.typeOfExaminationEdit.id = id;
    this.roomService.getRoomTypes().subscribe(res => {
      this.roomTypes = res;
      this.roomTypes.forEach(type => {
        if(type.name == typeOfExamination1.typeName) this.typeOfExaminationEdit.roomTypeId = type.roomTypeId;
      });
      this.editTypeOfExaminationModal.show();
    });
  }

  updateTypeOfExamination() {
    this.typesOfExaminationService.updateTypeOfExamination(this.typeOfExaminationEdit).subscribe(
      (data: TypeOfExamination) =>     this.ngOnInit(),
      error => { this.errorMessage = error.error.message;  console.log(this.errorMessage); }
    )
    this.editTypeOfExaminationModal.hide();
  }

  removeTypeOfExaminationModal(id: number) {
    this.typeOfExaminationId = id;
    this.deleteTypeOfExaminationModal.show();
  }

  removeTypeOfExamination() {
    this.typesOfExaminationService.deleteTypeOfExamination(this.typeOfExaminationId).subscribe( res => this.ngOnInit())
    this.deleteTypeOfExaminationModal.hide();
  }
}