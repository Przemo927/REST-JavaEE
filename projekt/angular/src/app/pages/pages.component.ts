import {Component, OnInit, DoCheck} from "@angular/core";
import {Page} from "../page";
import  { PageService } from "../service/page.service";
import { DataService } from '../service/data.service';
import {DiscoveryPageService} from "../service/discoverypage.service";
import {Router} from "@angular/router";
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit, DoCheck {

  private allPreparedPages: Page[];
  private index=1;
  private preparedPagesToShowOnScreen:Page[];
  private currentPage:any;
  private pageService: any;

  constructor(private dataService:DataService,private router:Router,private http:HttpClient) {
    if(this.router.url==='/'){
      this.pageService=new DiscoveryPageService(this.http);
    }
  }
  ngDoCheck(){
    if(this.index<1){
      this.index=1;
    }
    if(this.allPreparedPages!==undefined && this.index>this.allPreparedPages.length){
      this.index=this.allPreparedPages.length;
    }
    if(document.getElementById(this.index.toString())!==null && this.currentPage!==undefined){
      this.changeBackGroundColorOfPage();
    }
  }

  ngOnInit() {
    this.givePreparedPagesDependsOfElementsOnOnePage(5);
  }

  previousPage(){
    this.index-=1;
    if(this.index<this.preparedPagesToShowOnScreen[0].numberPage && this.index>0){
      this.preparedPagesToShowOnScreen=this.allPreparedPages.slice(this.index-1,this.index+4);
    }
    this.pageService.decreaseLimit().subscribe(elements => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }
  nextPage(){
    this.index+=1;
    let lastIndexOfVisiblePage=this.preparedPagesToShowOnScreen.length-1;
    if(this.index>this.preparedPagesToShowOnScreen[this.preparedPagesToShowOnScreen.length-1].numberPage && this.index<=this.allPreparedPages.length-1){
      this.preparedPagesToShowOnScreen=this.allPreparedPages.slice(this.index-5,this.index);
    }
    this.pageService.increaseLimit().subscribe(elements => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }
  changePage(numberPage:number){
    this.index=numberPage;
  }
  changeBackGroundColorOfPage(){
    this.currentPage.style.backgroundColor = 'white';
    this.currentPage=document.getElementById(this.index.toString());
    this.currentPage.style.backgroundColor='#ddd';
  }
  givePreparedPagesDependsOfElementsOnOnePage(quantityElementsOnOnePage: number):void {
    this.pageService.getQuantityOfAllElements().subscribe(response => {
      const quantityOfAllElements = response['Message'];
      this.allPreparedPages = this.pageService.preparePages(quantityElementsOnOnePage, quantityOfAllElements);
      this.getFirstVisiblePagesForPagination(5);
      this.chooseElementsForFirstPage();
    });
  }

  private getFirstVisiblePagesForPagination(numberOfFirstPages:number){
    this.preparedPagesToShowOnScreen=this.allPreparedPages.slice(0,numberOfFirstPages)
  }
  private chooseElementsForFirstPage():void{
    this.pageService.chooseFirstPage().subscribe((elements=>{
      this.currentPage= this.pageService.markFirstPage();
      this.dataService.addBehaviourSource(elements);
    }));
  }

  getelementsForActualPage(numberOfActualPage: number) {
    this.pageService.getElementsForActualPage(numberOfActualPage).subscribe((elements) => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }

}
