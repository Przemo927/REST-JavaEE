import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearcheventbycityComponent } from './searcheventbycity.component';

describe('SearcheventbycityComponent', () => {
  let component: SearcheventbycityComponent;
  let fixture: ComponentFixture<SearcheventbycityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearcheventbycityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearcheventbycityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
