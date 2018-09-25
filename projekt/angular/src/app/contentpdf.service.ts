import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class ContentPdfService {

  private dataSource$;
  public currentData;
  constructor() { }

  addBehaviourSource(elements:any){
    this.dataSource$= new BehaviorSubject<any>(elements);
    this.currentData=this.dataSource$.asObservable();
  }
  changeData(elements:any){
    this.dataSource$.next(elements);
  }

}
