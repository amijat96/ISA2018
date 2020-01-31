import { Component, OnInit, ViewChild } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService} from 'src/app/service/user.service';
import { AddressService} from 'src/app/service/address.service';
import { Country } from 'src/app/model/country';
import { City } from 'src/app/model/city';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.scss']
})
export class MyAccountComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;
  
  user = new User();
  newPassword = '';
  oldPassword = '';
  countries: Country[] = [];
  cities: City[] = [];
  errorMessage: string = '';
  validatingForm: FormGroup;

  constructor(private userService: UserService, private addressService: AddressService) {
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
      console.log(res.id);
      this.getCountries();
      this.getCitiesByCountryId(res.countryId);
    });
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
    console.log('update ' + this.user.id);
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
