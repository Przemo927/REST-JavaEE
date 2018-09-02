

import {Observable} from "rxjs/Observable";
import {of} from "rxjs/observable/of";
export class ErrorHandler {
  public static handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);
      console.log(`${operation} failed: ${error.message}`);

      if(error.error.invalidFieldList!==undefined){
        return of(error.error);
      }else{
        return of(result as T);
      }
    };
  }
}
