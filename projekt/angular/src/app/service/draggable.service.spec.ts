import { TestBed, inject } from '@angular/core/testing';

import { DraggableService } from './draggable.service';

describe('DraggableeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DraggableService]
    });
  });

  it('should be created', inject([DraggableService], (service: DraggableService) => {
    expect(service).toBeTruthy();
  }));
});
