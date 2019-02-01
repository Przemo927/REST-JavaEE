import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { Comment } from '../model/comment';
import {ErrorHandler} from "../errorhandler";


@Injectable()
export class CommentService {

  constructor(private http: HttpClient) { }

  private commentUrl='http://localhost:8080/projekt/api/comment';

  getComments(): Observable<Comment[]> {
  return this.http.get<Comment[]>(this.commentUrl)
  .pipe(tap(comment => console.log(`fetched comments`)),
      catchError(ErrorHandler.handleError<Comment[]>('getComments',[]))
    );
  }

  getCommentsById(id:number):Observable<Comment[]>{
    return this.http.get<Comment[]>(this.commentUrl+'/'+id)
    .pipe(tap(comment=>console.log('fetched comments for discovery id='+id)),
    catchError(ErrorHandler.handleError<Comment[]>('getCommentById',[]))
    );
  }


   addComment (comment: Comment): Observable<any> {
  return this.http.post<any>(this.commentUrl,comment).pipe(
    tap(comment => console.log('Comment added')),
    catchError(ErrorHandler.handleError<any>('addComment'))
  );
}
}
