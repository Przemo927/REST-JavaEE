import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidatedtextareaComponent } from './validatabletextarea.component';

describe('ValidatedtextareaComponent', () => {
  let component: ValidatedtextareaComponent;
  let fixture: ComponentFixture<ValidatedtextareaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ValidatedtextareaComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidatedtextareaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
