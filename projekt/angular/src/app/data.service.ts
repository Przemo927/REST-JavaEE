import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Injectable()
export class DataService<T> {

  private dataSource$;
  public currentData;
  constructor() { }

  addBehaviourSource(elements:T){
    this.dataSource$= new BehaviorSubject<T>(elements);
    this.currentData=this.dataSource$.asObservable();
  }
  changeData(elements:T){
    console.log(elements);
    this.dataSource$.next(elements);
  }

}
