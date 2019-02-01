import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";
import {ErrorHandler} from "../errorhandler";

@Injectable()
export class VoteService {

  private voteUrl='http://localhost:8080/projekt/api/vote';
  constructor(private http: HttpClient) { }

  addVote(voteType:string,discoveryId:number){
        return this.http.get(this.voteUrl+'?vote='+voteType+'&discoveryId='+discoveryId)
          .pipe(tap(vote => console.log('vote Added')),
            catchError(ErrorHandler.handleError<any>('add Vote'))
          );

  }

}
