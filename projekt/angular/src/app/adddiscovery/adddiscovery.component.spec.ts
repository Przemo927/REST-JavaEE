import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdddiscoveryComponent } from './adddiscovery.component';

describe('AdddiscoveryComponent', () => {
  let component: AdddiscoveryComponent;
  let fixture: ComponentFixture<AdddiscoveryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdddiscoveryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdddiscoveryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
