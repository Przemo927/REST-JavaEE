import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { Event } from '../event';
import {ErrorHandler} from "../errorhandler";
import {UrlUtils} from "../UrlUtils";
import {EndPoint} from "../endpoint.enum";

@Injectable()
export class EventService {

  constructor(private http: HttpClient) { }

 private eventUrl=UrlUtils.generateBasicUrl(window.location)+EndPoint.events;

getEvents(): Observable<Event[]> {
return this.http.get<Event[]>(this.eventUrl)
.pipe(tap(function(events){
            for(let event of events){
              event.timestamp=new Date(event.timestamp);
            }
            }),
      catchError(ErrorHandler.handleError<Event[]>('getEvents',[]))
    );
}

addEvent(event: Event): Observable<Event> {
  return this.http.post<Event>(this.eventUrl, event)
  .pipe(tap((event: Event) => console.log(`added event w/ id=${event.id}`)),
    catchError(ErrorHandler.handleError<Event>('addEvent'))
  );
}

getEventsByNameOfCity(nameOfCity:string):Observable<Event[]> {
    return this.http.get<Event[]>(this.eventUrl+'?city='+nameOfCity)
     .pipe(tap(function(events){
            for(let event of events){
              event.timestamp=new Date(event.timestamp);
            }
            }),
    catchError(ErrorHandler.handleError<Event[]>('getEventsByCity',[]))
  );
}

getEventsByPosition(latitude:number,longtitude:number,distance:number):Observable<Event[]>{
    return this.http.get<Event[]>(this.eventUrl+'/position?x='+latitude+'&y='+longtitude+'&distance='+distance)
    .pipe(tap(function(events){
            for(let event of events){
              event.timestamp=new Date(event.timestamp);
            }
            }),
    catchError(ErrorHandler.handleError<Event[]>('getEventsByPosition',[]))
    );
}

}
