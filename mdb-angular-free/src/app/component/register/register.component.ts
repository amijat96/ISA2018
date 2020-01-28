import { Component, OnInit, ViewChild} from '@angular/core';
import { Register } from 'src/app/model/register';
import { AddressService } from 'src/app/service/address.service';
import { Country } from 'src/app/model/country';
import { City } from 'src/app/model/city';
import { AuthService } from 'src/app/service/auth.service';
import { User } from 'src/app/model/user';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import { PasswordValidator } from 'src/app/validator/password-validator';
import * as moment from 'moment';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;

  acceptedTerms: boolean = false;
  countries: Country[] = [];
  cities: City[] = [];
  errorMessage: string = "";
  validatingForm: FormGroup;
  passwordForm: FormGroup;
  countryId: number = 0;
  dateOfBirth: string ="";
  register = new Register();
  hidden: boolean = !(window.location.href == 'http://localhost:4200/admin-profile/medical-staff/register');
  doctor: boolean = true;
  link : string = '/';

  constructor(private addressService: AddressService, private authService: AuthService, private formBuilder: FormBuilder) {
    this.getCountries();
    this.passwordForm = this.formBuilder.group({
      passwordControl: [null, [ Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      confirmPasswordControl: ['', Validators.required]
    },
    {
      validators:  PasswordValidator.MatchPassword
    });
   }

  get nameInput() { return this.validatingForm.get('nameControl'); }
  get lastNameInput() { return this.validatingForm.get('lastNameControl'); }
  get usernameInput() { return this.validatingForm.get('usernameControl'); }
  get emailInput() { return this.validatingForm.get('emailControl'); }
  get streetInput() { return this.validatingForm.get('streetControl'); }
  get passwordInput() { return this.passwordForm.get('passwordControl'); }
  get confirmPasswordInput() {return this.passwordForm.get('confirmPasswordControl')}
  get jboInput() { return this.validatingForm.get('jboControl'); }


  ngOnInit() {
    this.validatingForm = new FormGroup({
      nameControl: new FormControl(null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      lastNameControl: new FormControl(null, [Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      usernameControl: new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
      emailControl: new FormControl(null, [Validators.required, Validators.email]),
      streetControl: new FormControl(null, [Validators.required, Validators.minLength(6), Validators.maxLength(128)]),
      jboControl: new FormControl(null, [Validators.required, Validators.minLength(13), Validators.maxLength(13)]),

    });
  }

  onSubmit() {
    if(!this.hidden) {
      this.link ='/admin-profile/medical-staff';
      this.register.clinicId = Number(localStorage.getItem('clinicId'));
      if(this.doctor) this.register.roleId = 3;
      else this.register.roleId = 4;
    }
    this.register.password = this.passwordForm.controls["passwordControl"].value;
    this.register.dateOfBirth = (moment(this.dateOfBirth).format("YYYY-MM-DD"));
    this.authService.register(this.register).subscribe(
      (data: User) =>  this.successModal.show(),
      error => { this.errorMessage = error.error.message;  this.errorModal.show(); }
    );
  }

  getCountries() {
    this.addressService.getCountries().subscribe(
      (data: Country[]) => this.countries = data,
      error => console.log(error)
    );
  }

  getCitiesByCountryId(countryId: number) {
    this.addressService.getCitiesByCountryId(countryId).subscribe(
      (data: City[]) => this.cities = data,
      error => console.log(error)
    );
  }

  onCountryChanged(event) {
    this.cities = [];
    this.getCitiesByCountryId(this.countryId);
  }

  changeRole(r: boolean) {
    this.doctor = r;
  }
}
