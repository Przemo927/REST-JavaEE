import {Injectable} from "@angular/core";
import {Page} from "./page";
import {Observable} from "rxjs/Rx";
import {HttpClient} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";
import {ErrorHandler} from "./errorhandler";
import {UrlUtils} from "./UrlUtils";
import {BaseUrl} from "./baseurl.enum";

@Injectable()
export class PageService <T> {
  actualPage: Page= null;
  preparedPages: Page[];
  private url: string;

  constructor(public http: HttpClient, url: string) {
    this.url = url;
  }

  setUrl(url:string){
    this.url=url;
  }

  preparePages(quantityOnOnePage: number, quantityOfAllElements: number): Page[] {
    let pages = 0;
    if ((quantityOfAllElements % quantityOnOnePage) === 0) {
      pages = quantityOfAllElements / quantityOnOnePage;
    }else {
      pages = Math.floor(quantityOfAllElements / quantityOnOnePage) + 1;
    }
    this.preparedPages = Array(pages).fill(0).map((x, i) => new Page(i + 1, i * quantityOnOnePage, quantityOnOnePage));

    return this.preparedPages;
  }
  getElementsForActualPage<T>(numberOfActualPage: number): Observable<T[]> {
    if (this.actualPage == null || (this.actualPage != null && this.actualPage.numberPage !== numberOfActualPage)) {
      this.giveActualPage(numberOfActualPage);
      return this.getElementsWithLimit<T>(this.actualPage.firstIndex, this.actualPage.quantity);
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
  chooseFirstPage<T>(): Observable<T[]> {
    this.giveActualPage(1);
    return this.getElementsWithLimit<T>(this.actualPage.firstIndex, this.actualPage.quantity);
  }

  increaseLimit<T>(): Observable<T[]> {
    if (this.actualPage != null && this.actualPage.numberPage < this.preparedPages.length) {
      this.giveNextPage();
      return this.getElementsWithLimit<T>(this.actualPage.firstIndex, this.actualPage.quantity);
    }else {
      return Observable.empty<T[]>();
    }
  }

  decreaseLimit<T>(): Observable<T[]> {
    if (this.actualPage != null && this.actualPage.numberPage > 1) {
      this.givePreviousPage();
      console.log(this.actualPage.firstIndex, this.actualPage.quantity);
      return this.getElementsWithLimit<T>(this.actualPage.firstIndex, this.actualPage.quantity);
    }else {
      return Observable.empty<T[]>();
    }
  }

  getQuantityOfAllElements(): Observable<any> {
    return this.http.get<any>(this.url + BaseUrl.urlSeparator+ 'quantity')
      .pipe(tap((e) => console.log('fetched quantity of elements')),
        catchError(ErrorHandler.handleError<any>('getQuantityOfAllElements'))
      );
  }
  private givePreviousPage(): void {
    this.actualPage = this.preparedPages[this.actualPage.numberPage - 2];
  }

  private giveNextPage(): void {
    this.actualPage = this.preparedPages[this.actualPage.numberPage];
  }

  private giveActualPage(numberOfActualPage: number): void {
    this.actualPage = this.preparedPages[numberOfActualPage - 1];
  }
}
