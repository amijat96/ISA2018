import { Component, OnInit, ViewChild } from '@angular/core';
import { Clinic } from 'src/app/model/clinic';
import { Country } from 'src/app/model/country';
import { City } from 'src/app/model/city';
import { ClinicService } from 'src/app/service/clinic.service';
import { AddressService } from 'src/app/service/address.service';
import { ModalDirective } from 'angular-bootstrap-md';
import { FormGroup, Validators, FormControl} from '@angular/forms';
import { MapsAPILoader, AgmMap } from '@agm/core';

declare var google: any;

interface Location {
  lat: number;
  lng: number;
}

@Component({
  selector: 'app-clinic-profile',
  templateUrl: './clinic-profile.component.html',
  styleUrls: ['./clinic-profile.component.scss']
})

export class ClinicProfileComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;
  
  geocoder:any;
  clinic = new Clinic();
  countries: Country[] = [];
  cities: City[] = [];
  errorMessage: string = '';
  validatingForm: FormGroup;
  public location:Location = {
    lat: 51.678418,
    lng: 7.809007
  };
  constructor(public mapsApiLoader: MapsAPILoader, private addressService: AddressService, private clinicService: ClinicService) { 
    this.validatingForm = new FormGroup({
      nameControl: new FormControl(null, [ Validators.required, Validators.minLength(2), Validators.maxLength(40)]),
      descriptionControl: new FormControl(null,[Validators.maxLength(500)]),
      streetControl: new FormControl(null, [ Validators.required, Validators.minLength(3), Validators.maxLength(40)]),
      workTimeStartControl: new FormControl(null,[ Validators.required]),
      workTimeEndControl: new FormControl(null,[ Validators.required])
    })
  }

  get nameInput() { return this.validatingForm.get('nameControl'); }
  get descriptionInput() { return this.validatingForm.get('descriptionControl'); }
  get streetInput() {return this.validatingForm.get('streetControl'); }
  get workTimeStartInput() {return this.validatingForm.get('workTimeStartControl'); }
  get workTimeEndInput() {return this.validatingForm.get('workTimeEndControl'); }

  ngOnInit() {
    this.mapsApiLoader.load().then(() => { 
      this.clinicService.getClinic().subscribe(res => {
        this.clinic = res;
        this.getCountries();
        this.getCitiesByCountryId(this.clinic.countryId);
        this.geocoder = new google.maps.Geocoder();
      });
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
        var country: string = '';
        var city: string = '';
        this.countries.forEach(c => { if(c.countryId == this.clinic.countryId) country = c.name});
        this.cities.forEach(c => { if(c.cityId == this.clinic.cityId) city = c.name});
        this.findLocation(country + ' ' + city + ' ' + this.clinic.street); 
      },
      error => console.log(error)
    );
  }

  onCountryChanged(event) {
    this.cities = [];
    this.getCitiesByCountryId(this.clinic.countryId);
  }

  abort() {
    this.clinicService.getClinic().subscribe(res => {
      this.clinic = res;
      this.getCountries();
      this.getCitiesByCountryId(this.clinic.countryId);
    });
  }

  updateClinic() {
    this.clinicService.updateClinic(this.clinic).subscribe((data: Clinic) => {
      this.successModal.show();
      localStorage.setItem('clinicName', this.clinic.name);
    },
    error => { this.errorMessage = error.error.message;  this.errorModal.show()}
    );
  }

  reload() {
    this.successModal.hide();
    window.location.reload();
  }

  findLocation(address) {
    if (!this.geocoder) this.geocoder = new google.maps.Geocoder()
    this.geocoder.geocode({
      'address': address
    }, (results, status) => {
      if (status == google.maps.GeocoderStatus.OK) {      
        if (results[0].geometry.location) {
          this.location.lat = results[0].geometry.location.lat();
          this.location.lng = results[0].geometry.location.lng();
        }
      } else {
        alert("Sorry, this search produced no results.");
      }
    })
  }
}
