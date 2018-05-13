import {Component, OnInit} from "@angular/core";
import {Page} from "../page";
import  { DiscoveryPageService } from "../discoverypage.service";
import { Discovery } from '../discovery';
import { DataService } from '../data.service';

@Component({
  selector: 'app-pages',
  templateUrl: './pages.component.html',
  styleUrls: ['./pages.component.css']
})
export class PagesComponent implements OnInit {

  private preparedPages: Page[];
  private discoveries:Discovery[];
  private currentPage:any;

  constructor(private pageService: DiscoveryPageService,private dataService:DataService<Discovery[]>) { }

  ngOnInit() {
    console.log('pages');
    this.givePreparedPagesDependsOfElementsOnOnePage(5);
  }
  previousPage(){
    this.pageService.decreaseLimit<Discovery>().subscribe(discoveries => {
      if (discoveries != null) {
        this.discoveries = discoveries;
        this.dataService.changeData(discoveries);
      }
    });
  }
  nextPage(){
    this.pageService.increaseLimit<Discovery>().subscribe(discoveries => {
      if (discoveries != null) {
        this.discoveries = discoveries;
        this.dataService.changeData(discoveries);
      }
    });
  }
  changeBackGroundColorOfPage(event){
    this.currentPage=this.pageService.changeBackGroundColorOfPage(this.currentPage,event);
  }
  givePreparedPagesDependsOfElementsOnOnePage(quantityElementsOnOnePage: number):void {
    this.pageService.getQuantityOfAllElements().subscribe(q => {
      const quantityOfAllDiscoveries = q['Message'];
      this.preparedPages = this.pageService.preparePages(quantityElementsOnOnePage, quantityOfAllDiscoveries);
      this.chooseFirstPage();
    });
  }

  private chooseFirstPage():void{
    this.pageService.chooseFirstPage<Discovery>().subscribe((discoveries=>{
      this.discoveries=discoveries;
      this.currentPage= this.pageService.markFirstPage();
      this.dataService.addBehaviourSource(discoveries);
    }));
  }

  getDiscoveriesForActualPage(numberOfActualPage: number) {
    this.pageService.getElementsForActualPage<Discovery>(numberOfActualPage).subscribe((discoveries) => {
      if (discoveries != null) {
        this.discoveries = discoveries;
        this.dataService.changeData(discoveries);
      }
    });
  }

}
