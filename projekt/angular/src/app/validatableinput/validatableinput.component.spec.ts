import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidatableinputComponent } from './validatableinput.component';

describe('ValidatableinputComponent', () => {
  let component: ValidatableinputComponent;
  let fixture: ComponentFixture<ValidatableinputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidatableinputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidatableinputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
