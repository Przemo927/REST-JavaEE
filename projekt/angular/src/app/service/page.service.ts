import {Injectable} from "@angular/core";
import {Page} from "../model/page";
import {Observable} from "rxjs/Rx";
import {HttpClient} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";
import {ErrorHandler} from "../errorhandler";
import {UrlUtils} from "../UrlUtils";
import {BaseUrl} from "../enum/baseurl.enum";

@Injectable()
export class PageService <T> {
  private url: string;

  constructor(public http: HttpClient) {
  }

  setUrl(url:string){
    this.url=url;
  }

  preparePages(quantityOnOnePage: number, quantityOfAllElements: number): Page[] {
    let pages:number;
    if ((quantityOfAllElements % quantityOnOnePage) === 0) {
      pages = quantityOfAllElements / quantityOnOnePage;
    }else {
      pages = Math.floor(quantityOfAllElements / quantityOnOnePage) + 1;
    }
    return Array(pages).fill(0).map((x, i) => new Page(i + 1, i * quantityOnOnePage, quantityOnOnePage));
  }
  getElementsForActualPage<T>(actualPage: Page): Observable<T[]> {
    if (actualPage != null) {
      return this.getElementsWithLimit<T>(actualPage.firstIndex, actualPage.quantity);
    }else {
      return Observable.empty<T[]>();

    }
  }

  markFirstPage():any {
    let currentPage= document.getElementById('1');
    currentPage.style.backgroundColor = "#ddd";
    return currentPage;
  }
  getElementsWithLimit<T>(firstIndex: number, quantity: number): Observable<T[]> {
    let endPoint=UrlUtils.addParameterToUrl(this.url,"beginWith",firstIndex);
    endPoint=UrlUtils.addParameterToUrl(endPoint,"quantity",quantity);
    return this.http.get<T[]>(endPoint)
      .pipe(tap((e) => console.log('fetched elements with limit')),
        catchError(ErrorHandler.handleError<any>('getElementsWithLimit', []))
      );
  }
  chooseFirstPage<T>(firstPage:Page): Observable<T[]> {
    return this.getElementsWithLimit<T>(firstPage.firstIndex, firstPage.quantity);
  }

  getQuantityOfAllElements(): Observable<any> {
    return this.http.get<any>(this.url + BaseUrl.urlSeparator+ 'quantity')
      .pipe(tap((e) => console.log('fetched quantity of elements')),
        catchError(ErrorHandler.handleError<any>('getQuantityOfAllElements'))
      );
  }
}
