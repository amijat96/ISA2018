import { Component, OnInit } from '@angular/core';
import { ExaminationService } from 'src/app/service/examination.service';
import { ReportService } from 'src/app/service/report.service';
import { DiagnosisService } from 'src/app/service/diagnosis.service';
import { MedicineService } from 'src/app/service/medicine.service';
import { ActivatedRoute } from '@angular/router';
import { Examination } from 'src/app/model/examination';
import { Medicine } from 'src/app/model/medicine';
import { Diagnosis } from 'src/app/model/diagnosis';
import { Report } from 'src/app/model/report';
import { DatePipe } from '@angular/common';
import { DoctorReportRequest } from 'src/app/model/doctorReportRequest';

@Component({
  selector: 'app-doctor-report',
  templateUrl: './doctor-report.component.html',
  styleUrls: ['./doctor-report.component.scss']
})
export class DoctorReportComponent implements OnInit {

  examinationId: number = 0;
  examination: Examination = new Examination();
  medicines: Medicine[] = [];
  diagnoses: Diagnosis[] = [];
  selectedMedicines: Medicine[] = [];
  selectedDiagnoses: Diagnosis[] = [];
  report: DoctorReportRequest = new DoctorReportRequest();
  headElements = ['ID', 'Name', 'Description', ''];
  dateTime:string = '';

  constructor(
    private route: ActivatedRoute, 
    private examinationService: ExaminationService,
    private reportService: ReportService,
    private diagnosisService: DiagnosisService,
    private medicineService: MedicineService,
    private datePipe: DatePipe
    ) { 
      this.examinationId = this.route.snapshot.params['id'];
      this.examinationService.getExamination(this.examinationId).subscribe(res => {
        this.examination = res;
        this.dateTime = this.datePipe.transform(res.dateTime,"MMM d, y, HH:mm:ss");
        console.log(res);
      })
      this.medicineService.getMedicines().subscribe(res => {
        this.medicines = res;
        console.log(res);
      })
      this.diagnosisService.getDiagnoses().subscribe(res => {
        this.diagnoses = res;
        console.log(res);
      })
    }

  ngOnInit() {

  }

  addDiagnosis(diagnosis: Diagnosis) {
    this.selectedDiagnoses.push(diagnosis);
    this.diagnoses.splice(this.diagnoses.indexOf(diagnosis),1);
  }

  removeDiagnosis(diagnosis: Diagnosis) {
    this.diagnoses.push(diagnosis);
    this.selectedDiagnoses.splice(this.selectedDiagnoses.indexOf(diagnosis),1);
  }

  addMedicine(medicine: Medicine) {
    this.selectedMedicines.push(medicine);
    this.medicines.splice(this.medicines.indexOf(medicine),1);
  }

  removeMedicine(medicine: Medicine) {
    this.medicines.push(medicine);
    this.selectedMedicines.splice(this.selectedMedicines.indexOf(medicine),1);
  }
  
  createReport() {
    this.report.diagnoses = [];
    this.report.medicines = [];
    this.selectedDiagnoses.forEach(diagnosis => this.report.diagnoses.push(diagnosis.diagnosisId));
    this.selectedMedicines.forEach(medicine => this.report.medicines.push(medicine.medicineId));
    console.log(this.report);
    this.reportService.createReport(this.examination.examinationId, this.report).subscribe(res => {
      console.log(res);
      window.location.href = "doctor-profile/patients/" + this.examination.userUsername;
    })
  }
}
