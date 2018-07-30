import {async, ComponentFixture, TestBed} from "@angular/core/testing";

import {EncryptedComponent} from "./encrypted.component";

describe('EncryptedComponent', () => {
  let component: EncryptedComponent;
  let fixture: ComponentFixture<EncryptedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EncryptedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EncryptedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
