import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { Comment } from './comment';


@Injectable()
export class CommentService {

  constructor(private http: HttpClient) { }

  private commentUrl='http://localhost:8080/projekt/api/comment';


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



  getComments(): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.commentUrl)
      .pipe(tap(comment => console.log(`fetched comments`)),
        catchError(this.handleError<Comment[]>('getComments',[]))
      );
  }

  getCommentsById(id:number):Observable<Comment[]>{
    return this.http.get<Comment[]>(this.commentUrl+'/'+id)
      .pipe(tap(comment=>console.log('fetched comments for discovery id='+id)),
        catchError(this.handleError<Comment[]>('getCommentById',[]))
      );
  }


  addComment (comment: Comment,id:number): Observable<Comment> {
    return this.http.post<Comment>(this.commentUrl+'/'+id,comment).pipe(
      tap(comment =>
        console.log('Comment added')
      ),
      catchError(this.handleError<Comment>('addComment'))
    );
  }
}
