import { TestBed, inject } from '@angular/core/testing';

import { NameofcityService } from './nameofcity.service';

describe('NameofcityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NameofcityService]
    });
  });

  it('should be created', inject([NameofcityService], (service: NameofcityService) => {
    expect(service).toBeTruthy();
  }));
});
