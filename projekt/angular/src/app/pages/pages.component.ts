import {Component, Input, OnInit} from "@angular/core";
import {Page} from "../model/page";
import {DataService} from '../service/data.service';
import {Router} from "@angular/router";
import {HttpClient} from '@angular/common/http';
import {Discovery} from "../model/discovery";
import {PageService} from "../service/page.service";
import {UrlUtils} from "../UrlUtils";
import {EndPoint} from "../enum/endpoint.enum";

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit {

  @Input() typeOfElemnts:string;
  @Input() autoGenerate:string;
  private allPreparedPages: Page[];
  private index=1;
  private preparedPagesToShowOnScreen:Page[];
  private currentPage:any;
  private pageService: any;

  constructor(private dataService:DataService,private router:Router,private http:HttpClient) {
    this.pageService=new PageService<Discovery>(this.http);
  }

  ngOnInit() {
    if(this.typeOfElemnts=="discovery"){
      this.pageService.setUrl(UrlUtils.generateBasicUrl(window.location)+EndPoint.discoveries);
    }
    else if(this.typeOfElemnts=="event"){
      this.pageService.setUrl(UrlUtils.generateBasicUrl(window.location)+EndPoint.events);
    }
    if(this.autoGenerate === "true") {
      this.givePreparedPagesDependsOfElementsOnOnePage(5);
    }
  }

  givePreparedPagesDependsOfElementsOnOnePage(quantityElementsOnOnePage: number):void {
    this.pageService.getQuantityOfAllElements().subscribe(response => {
      const quantityOfAllElements = response['Message'];
      this.allPreparedPages = this.pageService.preparePages(quantityElementsOnOnePage, quantityOfAllElements);
      this.setFirstVisiblePagesForPagination(5);
      this.getElementsForFirstPage();
    });
  }

  private setFirstVisiblePagesForPagination(numberOfFirstPages:number){
    this.preparedPagesToShowOnScreen=this.allPreparedPages.slice(0,numberOfFirstPages)
  }
  private getElementsForFirstPage():void{
    this.pageService.chooseFirstPage(this.allPreparedPages[0]).subscribe((elements=>{
      this.currentPage= this.pageService.markFirstPage();
      this.dataService.addBehaviourSource(elements);
    }));
  }

  previousPage(){
    if(this.index>1) {
      this.index -= 1;
      if (this.index < this.preparedPagesToShowOnScreen[0].numberPage) {
        this.preparedPagesToShowOnScreen = this.allPreparedPages.slice(this.index - 1, this.index + 4);
      }
      this.getelementsForActualPage();
      setTimeout(()=>this.changeBackGroundColorOfPage(document.getElementById(this.index.toString())),1);
    }
  }
  nextPage(){
    if(this.index<this.allPreparedPages.length) {
      this.index += 1;
      if (this.index > this.preparedPagesToShowOnScreen[this.preparedPagesToShowOnScreen.length - 1].numberPage) {
        this.preparedPagesToShowOnScreen = this.allPreparedPages.slice(this.index - 5, this.index);
      }
      this.getelementsForActualPage();
      setTimeout(()=>this.changeBackGroundColorOfPage(document.getElementById(this.index.toString())),1);
    }
  }
  changePage(numberPage:number,currentPage:HTMLElement){
    this.index=numberPage;
    this.getelementsForActualPage();
    if(currentPage.localName==="div"){
      currentPage=currentPage.parentElement;
    }
    this.changeBackGroundColorOfPage(currentPage);
  }
  changeBackGroundColorOfPage(currentPage:HTMLElement){
    this.currentPage.style.backgroundColor = 'white';
    this.currentPage=currentPage;
    currentPage.style.backgroundColor='#ddd';
  }

  getelementsForActualPage() {
    this.pageService.getElementsForActualPage(this.allPreparedPages[this.index-1]).subscribe((elements) => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }

}
