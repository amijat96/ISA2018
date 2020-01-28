
import { BrowserModule } from '@angular/platform-browser';
import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { MDBBootstrapModule } from 'angular-bootstrap-md';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule,
   MatSelectModule,
   MatFormFieldModule,
   MatDatepickerModule,
   MatNativeDateModule } from '@angular/material';
import { httpInterceptorProviders } from './service/auth-interceptor';

import { AppComponent } from './app.component';
import { RegisterComponent } from './component/register/register.component';
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './component/admin/admin.component';
import { RoomsComponent } from './component/rooms/rooms.component';
import { MedicalStaffComponent } from './component/medical-staff/medical-staff.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: "admin-profile",
    component: AdminComponent,
    children: [
      {
        path: '',
        redirectTo:'rooms',
        pathMatch: 'full'
      },
      {
        path: "rooms",
        component: RoomsComponent
      },
      {
        path: "medical-staff",
        children: [
          {
            path: 'register',
            component: RegisterComponent
          },
          {
            path: '',
            component: MedicalStaffComponent
          }
        ]
      }
    ]
  },
  {
    path: "register",
    component: RegisterComponent
  }
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AdminComponent,
    RegisterComponent,
    RoomsComponent,
    MedicalStaffComponent
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MDBBootstrapModule.forRoot(),
    FormsModule,
    RouterModule.forRoot(routes),
    MatInputModule,
    MatSelectModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  exports: [RouterModule],
  providers: [httpInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
