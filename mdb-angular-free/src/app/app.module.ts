
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
import { AgmCoreModule, GoogleMapsAPIWrapper} from '@agm/core';
import { DatePipe } from '@angular/common'

import { AppComponent } from './app.component';
import { RegisterComponent } from './component/register/register.component';
import { HomeComponent } from './home/home.component';
import { AdminComponent } from './component/admin/admin.component';
import { RoomsComponent } from './component/rooms/rooms.component';
import { MedicalStaffComponent } from './component/medical-staff/medical-staff.component';
import { TypesOfExaminationComponent } from './component/types-of-examination/types-of-examination.component';
import { PriceListComponent } from './component/price-list/price-list.component';
import { ClinicProfileComponent } from './component/clinic-profile/clinic-profile.component';
import { MyAccountComponent } from './component/my-account/my-account.component';
import { ReportComponent } from './component/report/report.component';
import { ExaminationsComponent } from './component/examinations/examinations.component';
import { ExaminationComponent } from './component/examination/examination.component';
import { ScheduleComponent } from './component/schedule/schedule.component';
import { VacationsComponent } from './component/vacations/vacations.component';
import { VacationRequestsComponent } from './component/vacation-requests/vacation-requests.component';

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
      },
      {
        path: 'examinations',
        children : [
          {
            path:'',
            component : ExaminationsComponent
          },
          {
            path: 'predefined',
            component: ExaminationComponent
          },
          {
            path:'requests',
            component : ExaminationsComponent
          },
          {
          path: 'types-of-examination',
          component : TypesOfExaminationComponent
          },
          {
            path: ':id',
            component: ExaminationComponent
          }
        ]
      },
      {
        path: 'price-list',
        component: PriceListComponent
      },
      {
        path:'clinic-profile',
        component: ClinicProfileComponent
      },
      {
        path: 'my-account',
        component: MyAccountComponent
      },
      {
        path: 'report',
        component: ReportComponent
      },
      {
        path: 'schedule',
        component: ScheduleComponent
      },
      {
        path: 'vacations',
        children:
        [
          {
            path: '',
            component: VacationsComponent
          },
          {
            path: 'requests',
            component: VacationRequestsComponent
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
    MedicalStaffComponent,
    TypesOfExaminationComponent,
    PriceListComponent,
    ClinicProfileComponent,
    MyAccountComponent,
    ReportComponent,
    ExaminationsComponent,
    ExaminationComponent,
    ScheduleComponent,
    VacationsComponent,
    VacationRequestsComponent
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
    MatNativeDateModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyDYKwGWasRUj5FlVyAm50hmEOhnlXkCU4w'
    })
  ],
  exports: [RouterModule],
  providers: [httpInterceptorProviders, GoogleMapsAPIWrapper, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
