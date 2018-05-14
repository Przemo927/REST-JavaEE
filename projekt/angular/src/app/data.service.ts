import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class DataService {

  private dataSource$;
  public currentData;
  constructor() { }

  addBehaviourSource(elements:any){
    this.dataSource$= new BehaviorSubject<any>(elements);
    this.currentData=this.dataSource$.asObservable();
  }
  changeData(elements:any){
    console.log(elements);
    this.dataSource$.next(elements);
  }

}
