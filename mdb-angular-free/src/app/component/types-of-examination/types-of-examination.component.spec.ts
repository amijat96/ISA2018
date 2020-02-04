import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TypesOfExaminationComponent } from './types-of-examination.component';

describe('TypesOfExaminationComponent', () => {
  let component: TypesOfExaminationComponent;
  let fixture: ComponentFixture<TypesOfExaminationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TypesOfExaminationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TypesOfExaminationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
