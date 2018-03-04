import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { User } from './User';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';

@Injectable()
export class UserService {
private user:User;

  constructor(private http: HttpClient) { }

  private userUrl='http://localhost:8080/projekt/api/user';
  
  
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
  
  getUser(id: number): Observable<User> {
  console.log(this.userUrl+'/'+id);
  return this.http.get<User>(this.userUrl+'/'+id)
  .pipe(tap(user => console.log(`fetched user id=${id}`)),
      catchError(this.handleError<User>('getUser id=${id}'))
    );

}

getUsers(): Observable<User[]> {
  return this.http.get<User[]>(this.userUrl)
  .pipe(tap(users => console.log(`fetched users`)),
      catchError(this.handleError('getUsers', []))
    );
}

updateUser (user: User): Observable<any> {
  return this.http.put(this.userUrl, user).pipe(
    tap(_ => console.log(`updated user id=${user.id}`)),
    catchError(this.handleError<any>('updateUser'))
  );
}
}
