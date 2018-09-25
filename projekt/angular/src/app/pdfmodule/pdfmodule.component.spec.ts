import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PdfmoduleComponent } from './pdfmodule.component';

describe('PdfmoduleComponent', () => {
  let component: PdfmoduleComponent;
  let fixture: ComponentFixture<PdfmoduleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PdfmoduleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PdfmoduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
