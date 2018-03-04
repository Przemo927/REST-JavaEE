import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';

@Injectable()
export class VoteService {

  private voteUrl='http://localhost:8080/projekt/api/vote';
  constructor(private http: HttpClient) { }

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

  addVote(voteType:string,discoveryId:number){
        return this.http.get(this.voteUrl+'?vote='+voteType+'&discoveryId='+discoveryId)
          .pipe(tap(vote => console.log('vote Added')),
            catchError(this.handleError('add Vote'))
          );

  }

}
