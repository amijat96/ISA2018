import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicalStaffComponent } from './medical-staff.component';

describe('MedicalStaffComponent', () => {
  let component: MedicalStaffComponent;
  let fixture: ComponentFixture<MedicalStaffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MedicalStaffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MedicalStaffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
