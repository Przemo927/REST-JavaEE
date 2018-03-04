import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscoveriesComponent } from './discoveries.component';

describe('DiscoveriesComponent', () => {
  let component: DiscoveriesComponent;
  let fixture: ComponentFixture<DiscoveriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiscoveriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiscoveriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
