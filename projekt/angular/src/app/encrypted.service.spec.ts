import { TestBed, inject } from '@angular/core/testing';

import { EncryptedService } from './encrypted.service';

describe('EncryptedService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EncryptedService]
    });
  });

  it('should be created', inject([EncryptedService], (service: EncryptedService) => {
    expect(service).toBeTruthy();
  }));
});
