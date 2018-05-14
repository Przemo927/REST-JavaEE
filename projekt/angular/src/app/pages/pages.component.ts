import {Component, OnInit} from "@angular/core";
import {Page} from "../page";
import  { PageService } from "../page.service";
import { DataService } from '../data.service';
import {DiscoveryPageService} from "../discoverypage.service";
import {Router} from "@angular/router";
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit {

  private preparedPages: Page[];
  private currentPage:any;
  private pageService: any;

  constructor(private dataService:DataService,private router:Router,private http:HttpClient) { }
  ngOnInit() {
    this.givePreparedPagesDependsOfElementsOnOnePage(5);
    if(this.router.url==='/'){
      this.pageService=new DiscoveryPageService(this.http);
    }
  }

  previousPage(){
    this.pageService.decreaseLimit().subscribe(elements => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }
  nextPage(){
    this.pageService.increaseLimit().subscribe(elements => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }
  changeBackGroundColorOfPage(event){
    this.currentPage=this.pageService.changeBackGroundColorOfPage(this.currentPage,event);
  }
  givePreparedPagesDependsOfElementsOnOnePage(quantityElementsOnOnePage: number):void {
    this.pageService.getQuantityOfAllElements().subscribe(q => {
      const quantityOfAllElements = q['Message'];
      this.preparedPages = this.pageService.preparePages(quantityElementsOnOnePage, quantityOfAllElements);
      this.chooseFirstPage();
    });
  }

  private chooseFirstPage():void{
    this.pageService.chooseFirstPage().subscribe((elements=>{
      this.currentPage= this.pageService.markFirstPage();
      this.dataService.addBehaviourSource(elements);
    }));
  }

  getDiscoveriesForActualPage(numberOfActualPage: number) {
    this.pageService.getElementsForActualPage(numberOfActualPage).subscribe((elements) => {
      if (elements != null) {
        this.dataService.changeData(elements);
      }
    });
  }

}
