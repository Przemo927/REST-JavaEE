import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchbypositionComponent } from './searchbyposition.component';

describe('SearchbypositionComponent', () => {
  let component: SearchbypositionComponent;
  let fixture: ComponentFixture<SearchbypositionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchbypositionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchbypositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
