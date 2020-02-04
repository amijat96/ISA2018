import { Component, OnInit } from '@angular/core';
import { Login } from 'src/app/model/login';
import { AuthService } from 'src/app/service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  login: Login = new Login();
  success: boolean = true;
  errorMessage: string = "";
  loginSuccessful = true;

  constructor(private authService: AuthService, private router: Router) { console.log(this.router.url); }

  ngOnInit() {
  }

  onLogin() {
    this.authService.login(this.login).subscribe(
      (data: { tokenType: string, accessToken: string, role: string, clinicId: number, clinicName: string }) => { 
        this.authService.storeToken(data.accessToken);
        switch(data.role) {
          case "ROLE_ADMIN_CLINIC": this.authService.storeClinic(data.clinicId, data.clinicName); this.router.navigate(["admin-profile"]); break;
          default: this.router.navigate([""]);
        }
      },
      error => { this.errorMessage = error.error.message; this.success = false; this.loginSuccessful = false}
    );
  }

  hideLabel(event) {
    this.loginSuccessful = true;
  }

}
