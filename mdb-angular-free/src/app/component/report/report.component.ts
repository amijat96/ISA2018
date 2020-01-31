import { Component, OnInit } from '@angular/core';
import { ClinicService } from 'src/app/service/clinic.service';
import { ReportRequest } from 'src/app/model/reportRequest';
import { ReportResponse } from 'src/app/model/reportResponse';
import { DatePipe } from '@angular/common'

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})
export class ReportComponent implements OnInit {
  clinicGrade: number = 0;
  revenues:number;
  fromDate: Date;
  toDate: Date;
  frequency;
  values: number[] = [];
  colors: any[] = [];
  chartType: string = 'bar';
  chartDatasets: Array<any> = [
    { data: []}
  ];

  chartLabels: Array<any> = [];
  chartColors: Array<any> = [
    {
      backgroundColor: '#ffbb3388',
      borderColor: '#ffbb33',
      borderWidth: 2,
    }
  ];
  constructor(private clinicService: ClinicService, public datepipe: DatePipe) { }

  ngOnInit() {
    this.clinicService.getClinicGrade().subscribe(res => this.clinicGrade = res);
  }

  public chartOptions: any = {
    responsive: true,
    scales: {
      yAxes: [{
        ticks: {
          beginAtZero: true
        }
      }]
    }
  };

  public chartClicked(e: any): void { }
  public chartHovered(e: any): void { }

  changeFrequency(type: number) {
    this.frequency = type;
    this.getReport();
  }

  getReport() {
    if(this.fromDate < this.toDate) {
      //reset values
      this.chartDatasets = [
        { data: []}
      ];
      this.chartLabels = [];
      this.values = [];

      var report = new ReportRequest(this.datepipe.transform(this.fromDate, 'yyyy-MM-dd'), this.datepipe.transform(this.toDate, 'yyyy-MM-dd'), this.frequency);
      this.clinicService.getClinicReport(report).subscribe(
        (data: ReportResponse) => {
            this.revenues = data.revenues;
            data.reports.forEach(report => {
              this.values.push(report.numberOfExaminations);
              this.chartLabels.push(report.period);
            })
            this.chartDatasets = [
              { data: this.values}
            ];
        });
    }
  }
}