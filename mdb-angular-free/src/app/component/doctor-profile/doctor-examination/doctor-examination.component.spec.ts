import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DoctorExaminationComponent } from './doctor-examination.component';

describe('DoctorExaminationComponent', () => {
  let component: DoctorExaminationComponent;
  let fixture: ComponentFixture<DoctorExaminationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DoctorExaminationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DoctorExaminationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
