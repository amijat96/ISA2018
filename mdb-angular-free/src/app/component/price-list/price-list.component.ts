import { Component, OnInit, ViewChild, AfterViewInit, ChangeDetectorRef, HostListener} from '@angular/core';
import { TypeOfExaminationService } from 'src/app/service/type-of-examination.service';
import { PriceListService } from 'src/app/service/price-list.service';
import { PriceList } from 'src/app/model/priceList';
import { TypeOfExamination } from 'src/app/model/typeOfExamination';
import { MdbTablePaginationComponent, MdbTableDirective } from 'angular-bootstrap-md';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-price-list',
  templateUrl: './price-list.component.html',
  styleUrls: ['./price-list.component.scss']
})
export class PriceListComponent implements OnInit, AfterViewInit {
  @ViewChild(MdbTablePaginationComponent, { static: true }) mdbTablePagination: MdbTablePaginationComponent;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective
  @ViewChild('newPriceListItemModal', {static: false}) newPriceListItemModal: ModalDirective;
  @ViewChild('editPriceListItemModal', {static: false}) editPriceListItemModal: ModalDirective;
  @ViewChild('deletePriceListItemModal', {static: false}) deletePriceListItemModal: ModalDirective;
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;

  headElements = ['ID', 'Examination/Operation', 'Price (€)', 'Commands'];
  headElementsModel = ['priceListId', 'typeOfExaminationName', 'price'];
  validatingForm: FormGroup;
  validatingEditForm: FormGroup;
  typesOfExamination: TypeOfExamination[] = [];
  priceList: PriceList[] = [];
  priceListItem = new PriceList();
  editPriceListItem = new PriceList();
  priceListItemId = 0;
  previous: any = [];
  searchText: string = '';
  errorMessage: string = '';
  successModalMessage = '';
  selectedTypeOfExamination: TypeOfExamination = new TypeOfExamination();

  constructor(private typesOfExaminationService: TypeOfExaminationService, private priceListService: PriceListService, private cdRef: ChangeDetectorRef) {
    this.validatingForm = new FormGroup({
      priceControl: new FormControl(null, [ Validators.required, Validators.min(0)]),
    })

    this.validatingEditForm = new FormGroup({
      editPriceControl: new FormControl(null, [Validators.required, Validators.min(0)]),
    })
   }
   get priceInput() { return this.validatingForm.get('priceControl'); }
   get editPriceInput() { return this.validatingEditForm.get('editPriceControl'); }

  ngOnInit() {
    this.priceListService.getPriceList().subscribe(res => {
      this.priceList = res; 
      this.mdbTable.setDataSource(this.priceList);
      this.priceList= this.mdbTable.getDataSource();
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
      this.priceList = this.mdbTable.getDataSource(); 
    } 
    if (this.searchText) { 
      this.priceList =this.mdbTable.searchLocalDataByMultipleFields(this.searchText, ['typeOfExaminationName']); 
      this.mdbTable.setDataSource(prev); 
    }
  }

  createPriceListItemModal() {
    this.typesOfExaminationService.getTypesOfExamination().subscribe(res => this.typesOfExamination = res);
    this.newPriceListItemModal.show();
  }

  createPriceListItem() {
    this.priceListService.createPriceListItem(this.priceListItem).subscribe(
      (data: PriceList) =>  {
        this.successModalMessage = 'Your price list item created successfully';
        this.successModal.show();
        this.newPriceListItemModal.hide();
        this.ngOnInit();
      },
    error => { this.errorMessage = error.error.message;  this.errorModal.show();}
    )
  }

  updatePriceListItemModal(priceListItemToEdit: PriceList) {
    this.editPriceListItem = priceListItemToEdit;
    this.typesOfExaminationService.getTypesOfExamination().subscribe(res => this.typesOfExamination = res);
    this.typesOfExaminationService.getTypeOfExamination(this.editPriceListItem.typeOfExaminationId).subscribe(res => {
      this.selectedTypeOfExamination = res;
    })
    this.editPriceListItemModal.show();
  }

  updatePriceListItem() {
    this.priceListService.updatePriceListItem(this.editPriceListItem).subscribe(
      (data: PriceList) =>  {
        this.successModalMessage = 'Your price list item updated successfully';
        this.successModal.show();
        this.editPriceListItemModal.hide();
        this.ngOnInit();
      },
    error => { this.errorMessage = error.error.message;  this.errorModal.show();})
  }

  removePriceListItemModal(priceListItemToRemoveId: number) {
    this.priceListItemId = priceListItemToRemoveId;
    this.deletePriceListItemModal.show();
  }

  removePriceListItem() {
    this.priceListService.deletePriceListItem(this.priceListItemId). subscribe(res => this.ngOnInit());
    this.deletePriceListItemModal.hide();
  }
}
