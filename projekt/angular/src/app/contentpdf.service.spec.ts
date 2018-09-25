import { TestBed, inject } from '@angular/core/testing';

import { ContentpdfService } from './contentpdf.service';

describe('ContentpdfService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentpdfService]
    });
  });

  it('should be created', inject([ContentpdfService], (service: ContentpdfService) => {
    expect(service).toBeTruthy();
  }));
});
