import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditdiscoveryComponent } from './editdiscovery.component';

describe('EditdiscoveryComponent', () => {
  let component: EditdiscoveryComponent;
  let fixture: ComponentFixture<EditdiscoveryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditdiscoveryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditdiscoveryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
