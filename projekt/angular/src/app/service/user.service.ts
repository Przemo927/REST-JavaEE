import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { User } from '../user';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import {ErrorHandler} from "../errorhandler";

@Injectable()
export class UserService {
private user:User;

  constructor(private http: HttpClient) { }

  private userUrl='http://localhost:8080/projekt/api/user';

  getUser(id: number): Observable<User> {
  console.log(this.userUrl+'/'+id);
  return this.http.get<User>(this.userUrl+'/'+id)
  .pipe(tap(user => console.log(`fetched user id=${id}`)),
      catchError(ErrorHandler.handleError<User>('getUser id=${id}'))
    );

}

getUsers(): Observable<User[]> {
  return this.http.get<User[]>(this.userUrl)
  .pipe(tap(users => console.log(`fetched users`)),
      catchError(ErrorHandler.handleError<User[]>('getUsers', []))
    );
}

updateUser (user: User): Observable<any> {
  return this.http.put(this.userUrl, user).pipe(
    tap(_ => console.log(`updated user id=${user.id}`)),
    catchError(ErrorHandler.handleError<any>('updateUser'))
  );
}
}
