import { TestBed, inject } from '@angular/core/testing';

import { EventpositionService } from './eventposition.service';

describe('EventpositionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EventpositionService]
    });
  });

  it('should be created', inject([EventpositionService], (service: EventpositionService) => {
    expect(service).toBeTruthy();
  }));
});
