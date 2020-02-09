import { Component, OnInit, ViewChild } from '@angular/core';
import { ModalDirective, MdbTableDirective } from 'angular-bootstrap-md';
import { User } from 'src/app/model/user';
import { Country } from 'src/app/model/country';
import { City } from 'src/app/model/city';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { AddressService } from 'src/app/service/address.service';
import { TypeOfExaminationService } from 'src/app/service/type-of-examination.service';
import { TypeOfExamination } from 'src/app/model/typeOfExamination';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-my-account-doctor',
  templateUrl: './my-account-doctor.component.html',
  styleUrls: ['./my-account-doctor.component.scss']
})

export class MyAccountDoctorComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;
  @ViewChild(MdbTableDirective, { static: true }) mdbTable: MdbTableDirective

  user = new User();
  newPassword = '';
  oldPassword = '';
  countries: Country[] = [];
  cities: City[] = [];
  errorMessage: string = '';
  validatingForm: FormGroup;
  typesOfExamination: TypeOfExamination[] = [];
  headElements = ['ID', 'Name', 'Type', 'Specialization'];
  headElementsModel = ['id', 'name', 'typeName'];

  constructor(private userService: UserService, public datepipe: DatePipe, private addressService: AddressService, private typeOfExaminationService: TypeOfExaminationService) {
    this.validatingForm = new FormGroup({
      nameControl: new FormControl(null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      lastNameControl: new FormControl(null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      emailControl: new FormControl(null, [Validators.required, Validators.email]),
      streetControl: new FormControl(null, [Validators.required, Validators.minLength(6), Validators.maxLength(128)]),
    });
  }

  get nameInput() { return this.validatingForm.get('nameControl'); }
  get lastNameInput() { return this.validatingForm.get('lastNameControl'); }
  get emailInput() { return this.validatingForm.get('emailControl'); }
  get streetInput() { return this.validatingForm.get('streetControl'); }
  
  ngOnInit() {
    this.userService.getMyAccount().subscribe(res => {
      this.user = res;
      this.getCountries();
      this.getCitiesByCountryId(res.countryId);
      this.typeOfExaminationService.getTypesOfExamination().subscribe(typesOfExamination => {
        this.typesOfExamination = typesOfExamination; 
        this.setChecked();
      })
    });
  }

  change(type: TypeOfExamination) {
    this.typesOfExamination.forEach(element => {
      if(element.id == type.id) element.checked = !element.checked;
    });
  }
  
  setChecked() { 
    console.log(this.typesOfExamination)
    this.user.specializations.forEach(specialization => {
      this.typesOfExamination.forEach(type => {
        if(specialization == type.id) type.checked = true;
      })
    })
  }
  getCountries() {
    this.addressService.getCountries().subscribe(
      (data: Country[]) => this.countries = data,
      error => console.log(error)
    );
  }

  getCitiesByCountryId(countryId: number) {
    this.addressService.getCitiesByCountryId(countryId).subscribe(
      (data: City[]) => {
        this.cities = data;
      },
      error => console.log(error)
    );
  }

  onCountryChanged(event) {
    this.cities = [];
    this.getCitiesByCountryId(this.user.countryId);
  }

  compare(v1: string, v2: string): boolean {
    return v1 == v2;
  }

  updateUser() {
    this.user.specializations = [];
    this.typesOfExamination.forEach(type => {
      if(type.checked) this.user.specializations.push(type.id);
    })
    this.user.dateOfBirth = this.datepipe.transform(this.user.dateOfBirth, 'yyyy-MM-dd')
    this.userService.updateUser(this.user).subscribe(
      (data: User) => {
        this.successModal.show();
      },
      error => { this.errorMessage = error.error.message;  this.errorModal.show()}
    )
  }

  reload() {
    this.successModal.hide();
    window.location.reload();
  }
}
