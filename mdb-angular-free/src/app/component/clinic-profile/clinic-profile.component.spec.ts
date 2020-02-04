import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClinicProfileComponent } from './clinic-profile.component';

describe('ClinicProfileComponent', () => {
  let component: ClinicProfileComponent;
  let fixture: ComponentFixture<ClinicProfileComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClinicProfileComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClinicProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
