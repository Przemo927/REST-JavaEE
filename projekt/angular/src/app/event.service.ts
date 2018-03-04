import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { Event } from './event';

@Injectable()
export class EventService {

  constructor(private http: HttpClient) { }
   
 private eventUrl='http://localhost:8080/projekt/api/event';
  
   private handleError<T> (operation = 'operation', result?: T) {
  return (error: any): Observable<T> => {

    // TODO: send the error to remote logging infrastructure
    console.error(error); // log to console instead

    // TODO: better job of transforming error for user consumption
    console.log(`${operation} failed: ${error.message}`);

    // Let the app keep running by returning an empty result.
    return of(result as T);
  };
}

getEvents(): Observable<Event[]> {
return this.http.get<Event[]>(this.eventUrl)
.pipe(tap(function(e){
            for(let event of e){
            event.timestamp=new Date(event.timestamp);
            }
            
            }),
      catchError(this.handleError<Event[]>('getEvents',[]))
    );
}

addEvent(event: Event): Observable<Event> {
  return this.http.post<Event>(this.eventUrl, event)
  .pipe(tap((event: Event) => console.log(`added event w/ id=${event.id}`)),
    catchError(this.handleError<Event>('addEvent'))
  );
}

getEventsByNameOfCity(nameOfCity:string):Observable<Event[]> {
    return this.http.get<Event[]>(this.eventUrl+'?city='+nameOfCity)
     .pipe(tap(function(e){
            for(let event of e){
            event.timestamp=new Date(event.timestamp);
            }
            
            }),
    catchError(this.handleError<Event[]>('getEventsByCity',[]))
  );
}

getEventsByPosition(latitude:number,longtitude:number,distance:number):Observable<Event[]>{
    return this.http.get<Event[]>(this.eventUrl+'/position?x='+latitude+'&y='+longtitude+'&distance='+distance)
    .pipe(tap(function(e){
            for(let event of e){
            event.timestamp=new Date(event.timestamp);
            }
            
            }),
    catchError(this.handleError<Event[]>('getEventsByPosition',[]))
    );
}

}
