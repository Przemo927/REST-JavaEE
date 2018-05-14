import { TestBed, inject } from '@angular/core/testing';

import { PageserviceService } from './page.service';

describe('PageserviceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PageserviceService]
    });
  });

  it('should be created', inject([PageserviceService], (service: PageserviceService) => {
    expect(service).toBeTruthy();
  }));
});
