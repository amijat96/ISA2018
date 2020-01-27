import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';
@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
  
  clinicName = this.authService.getClinicName();

  constructor(private authService: AuthService) { }

  ngOnInit() {
  }

  rooms() {
    return window.location.href == "http://localhost:4200/admin-profile/rooms"
  }

  medicalStaff() {
    return window.location.href == "http://localhost:4200/admin-profile/medical-staff"
  }
}
