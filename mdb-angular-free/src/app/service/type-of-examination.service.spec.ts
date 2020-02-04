import { TestBed } from '@angular/core/testing';

import { TypeOfExaminationService } from './type-of-examination.service';

describe('TypeOfExaminationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TypeOfExaminationService = TestBed.get(TypeOfExaminationService);
    expect(service).toBeTruthy();
  });
});
