import {Injectable} from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs/observable/of';
import { Discovery} from './discovery';
import {ErrorHandler} from "./errorhandler";

@Injectable()
export class DiscoveryService {

  constructor(private http: HttpClient) { }

  private discovery: Discovery;
  private discoveryUrl= 'http://localhost:8080/projekt/api/discovery';

getDiscoveries(): Observable<Discovery[]> {
  return this.http.get<Discovery[]>(this.discoveryUrl)
  .pipe(tap((e) => console.log(`fetched discoveries`)),
      catchError(ErrorHandler.handleError<Discovery[]>('getDiscoveries', []))
    );

}

getDiscoveriesWithLimit(firstIndex: number, quantity: number): Observable<Discovery[]>{
    return this.http.get<Discovery[]>(this.discoveryUrl + '?beginWith=' + firstIndex + '&endWith=' + quantity)
    .pipe(tap((e) => console.log('fetched discoveries with limit')),
    catchError(ErrorHandler.handleError<Discovery[]>('getDiscoveriesWithlimit', []))
    );

}

getDiscovery (id: number): Observable<Discovery> {
    return this.http.get<Discovery>(this.discoveryUrl + '/' + id)
    .pipe(tap((e) => console.log(`fetched discovery id=${id}`)),
    catchError(ErrorHandler.handleError<Discovery>('getDiscovery id=${id}'))
    );
}

updateDiscovery (discovery: Discovery): Observable<Discovery> {
    return this.http.put<Discovery>(this.discoveryUrl, discovery).pipe(
    tap((e) => console.log(`updated discovery id=${discovery.id}`)),
    catchError(ErrorHandler.handleError<Discovery>('updateDiscovery'))
  );
}

addDiscovery(discovery: Discovery): Observable<any> {
    return this.http.post<any>(this.discoveryUrl, discovery).pipe(
    tap((e) => console.log('discovery was added'))
    , catchError(ErrorHandler.handleError<any>('addDiscovery'))
    );
}

deleteDiscovery(id: number): Observable<any> {
    return this.http.delete<any>(this.discoveryUrl + '/' + id).pipe(
    tap((e) => console.log(e.json()))
    , catchError(ErrorHandler.handleError<any>('deleteDiscovery'))
    );
}

getQuantityOfDiscoveries(): Observable<any> {
    return this.http.get<any>(this.discoveryUrl + '/quantity').pipe(
    tap((e) => console.log('Got quantity'), catchError(ErrorHandler.handleError<any>('getQuantityOfDiscoveries')))
    );
}


}
