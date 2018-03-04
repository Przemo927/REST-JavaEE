import { TestBed, inject } from '@angular/core/testing';

import { CheckUserService } from './check-user.service';

describe('CheckUserService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CheckUserService]
    });
  });

  it('should be created', inject([CheckUserService], (service: CheckUserService) => {
    expect(service).toBeTruthy();
  }));
});
