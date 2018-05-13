import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListofeventsComponent } from './listofevents.component';

describe('ListofeventsComponent', () => {
  let component: ListofeventsComponent;
  let fixture: ComponentFixture<ListofeventsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListofeventsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListofeventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
