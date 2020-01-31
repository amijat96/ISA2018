import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';
import { ModalDirective } from 'angular-bootstrap-md';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/model/user';
import { FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import { PasswordValidator } from 'src/app/validator/password-validator';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  @ViewChild('successModal', {static: false}) successModal: ModalDirective;

  clinicName = this.authService.getClinicName();
  disableModal = false;
  modalMessage = '';
  passwordForm: FormGroup;
  password:string = '';

  constructor(private authService: AuthService, private userService: UserService, private formBuilder: FormBuilder) { 
    this.passwordForm = this.formBuilder.group({
      passwordControl: [null, [ Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      confirmPasswordControl: ['', Validators.required]
    },
    {
      validators:  PasswordValidator.MatchPassword
    });
  }

  get passwordInput() { return this.passwordForm.get('passwordControl'); }
  get confirmPasswordInput() {return this.passwordForm.get('confirmPasswordControl')}
  
  ngOnInit() {
    this.userService.getMyAccount().subscribe(
      (data: User) => {
        if(data.passwordChanged) this.disableModal = false;
        else {
          this.disableModal = true;
          this.modalMessage = 'You must change your password';
          this.successModal.show();
        }
    })   
  }

  rooms() {
    return window.location.href == "http://localhost:4200/admin-profile/rooms"
  }

  medicalStaff() {
    return window.location.href == "http://localhost:4200/admin-profile/medical-staff" ||  
    window.location.href == "http://localhost:4200/admin-profile/medical-staff/register";
  }

  examinations() {
    return window.location.href == "http://localhost:4200/admin-profile/examinations/types-of-examination";
  }

  priceList() {
    return window.location.href == "http://localhost:4200/admin-profile/price-list";
  }
  clinicProfile() {
    if(window.location.href == "http://localhost:4200/admin-profile/clinic-profile") return '#81d4fa';
  }
  logOut() {
    this.authService.logOut();
  }

  changePasswordModal(){
    this.disableModal = false;
    this.modalMessage = 'Change password'
    this.successModal.show();
  }

  changePassword() {
    this.userService.changePassword(this.password).subscribe(
      res => {this.successModal.hide();
    })
  }
}
