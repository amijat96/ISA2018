import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from 'src/app/service/user.service';
import { ExaminationService } from 'src/app/service/examination.service';
import { User } from 'src/app/model/user';
import { ActivatedRoute } from '@angular/router';
import { PriceList } from 'src/app/model/priceList';
import { PriceListService } from 'src/app/service/price-list.service';
import { Examination } from 'src/app/model/examination';
import { TypeOfExamination } from 'src/app/model/typeOfExamination';
import { TypeOfExaminationService } from 'src/app/service/type-of-examination.service';
import { DatePipe } from '@angular/common';
import { ModalDirective } from 'angular-bootstrap-md';

@Component({
  selector: 'app-doctor-examination',
  templateUrl: './doctor-examination.component.html',
  styleUrls: ['./doctor-examination.component.scss']
})
export class DoctorExaminationComponent implements OnInit {
  @ViewChild('errorModal', {static: false}) errorModal: ModalDirective;

  doctor: User = new User();
  username: string = '';
  patient: User = new User();
  specializations: TypeOfExamination[] = [];
  examination: Examination = new Examination();
  priceList: PriceList[] = [];
  typeOfExamination: TypeOfExamination;
  examinationTime: string;
  examinationDate: Date;
  errorMessage: string = '';

  constructor(
    private userService: UserService, 
    private examinationService: ExaminationService,
    private route: ActivatedRoute,
    private priceListService: PriceListService,
    private datePipe: DatePipe,
    private typeOfExaminationService: TypeOfExaminationService 
    ) {}

  ngOnInit() {
    this.userService.getMyAccount().subscribe(res => {
      this.doctor = res;
      this.examination.doctorId = res.id;
      this.priceListService.getPriceList().subscribe(priceList => {
        this.priceList = priceList
        priceList.forEach(item => {
          this.doctor.specializations.forEach(spec => {
            if(spec == item.typeOfExaminationId)  {
              this.typeOfExaminationService.getTypeOfExamination(spec).subscribe(
                type => this.specializations.push(type)
                )
            }
          })
        })
      })
    });
    this.username = this.route.snapshot.params['username'];
    this.userService.getUserByUsername(this.username).subscribe(res => {
      this.patient = res;
      this.examination.userId = res.id;
    });
  }

  onTypeChanged(event) {
    this.priceList.forEach(item => {
      if(item.typeOfExaminationId == this.typeOfExamination.id) this.examination.priceListId = item.priceListId;
    })
  }

  createExamination() {
    console.log(this.examination);
    this.examination.typeId = this.typeOfExamination.roomTypeId;
    this.examinationService.createExamination(this.examination).subscribe(
        (data: Examination) => {
          window.location.href = 'http://localhost:4200/doctor-profile/patients/' + this.username;
        },
        error => {
          this.errorMessage = error.error.message;  
          this.errorModal.show();
        }
      )
  }

  OnDateTimeChange(event) {
    if(this.examinationTime != undefined && this.examinationDate != undefined) {
      this.examinationDate.setHours(Number(this.examinationTime.substr(0,2)), Number(this.examinationTime.substr(3,2)), 0);
      this.examination.dateTime = this.examinationDate;
    }
  }
}
