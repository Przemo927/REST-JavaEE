///<reference path="../check-user.service.ts"/>
import {
  Component, Input, OnInit, DoCheck, IterableDiffers, IterableDiffer} from '@angular/core';
import { DiscoveryService } from '../discovery.service';
import { Discovery } from '../discovery';
import { CheckUserService } from '../check-user.service';
import { Page } from '../page';
import { DiscoveryPageService } from '../discoverypage.service';
import {Observable} from 'rxjs/Rx';
import { DataService } from '../data.service';

@Component({
  selector: 'app-discoveries',
  templateUrl: './discoveries.component.html',
  styleUrls: ['./discoveries.component.css']
})
export class DiscoveriesComponent implements OnInit, DoCheck {


constructor(private discoveryService: DiscoveryService, private checkUserService: CheckUserService,
            private pageService: DiscoveryPageService, private iterableDiffers:IterableDiffers,
            private dataService:DataService<Discovery[]>) {
    this.iterableDiffer=iterableDiffers.find([]).create(null);
}

  discoveries: Discovery[]= [];
  private adminRole= false;
  private VOTE_UP= 'VOTE_UP';
  private VOTE_DOWN= 'VOTE_DOWN';
  private preparedPages: Page[];
  private currentPage: any;
  private iterableDiffer: IterableDiffer<any>;
  private htmlCollectionAsArray:Array<HTMLElement>;
  private firstIndexOfHidden=1;

  ngDoCheck() {
    let listOfElements=document.getElementById("discoveries").children;
    let htmlCollectionAsArray=[].slice.apply(listOfElements);
    let changes=this.iterableDiffer.diff(htmlCollectionAsArray);
    if(changes!==null && htmlCollectionAsArray.length>5) {
      this.firstIndexOfHidden=1;
      this.htmlCollectionAsArray=htmlCollectionAsArray;
      this.showFiveElements(this.firstIndexOfHidden);
    }
    if(this.dataService.currentData!==undefined) {
      this.dataService.currentData.subscribe((discoveries) => this.discoveries = discoveries);
    }
  }
  ngOnInit() {
      this.checkUserService.getUserInformation().subscribe(
          information => {
            if (information['role'] === 'admin') {
                this.adminRole = true;
            }

      });
    Observable.fromEvent(document,'scroll')
      .subscribe(<UiEvent>(scroll)=> {
        if(this.htmlCollectionAsArray!== undefined && this.firstIndexOfHidden<this.htmlCollectionAsArray.length && scroll.pageY>this.htmlCollectionAsArray[this.firstIndexOfHidden-1].offsetTop-200){
          this.showFiveElements(this.firstIndexOfHidden);
        }
      });
  }

  givePreparedPagesDependsOfElementsOnOnePage(quantityElementsOnOnePage: number):void {
    this.pageService.getQuantityOfAllElements().subscribe(q => {
            const quantityOfAllDiscoveries = q['Message'];
            this.chooseFirstPage();
    });

  }

  private chooseFirstPage():void{
    this.pageService.chooseFirstPage<Discovery>().subscribe((discoveries=>{
      this.discoveries=discoveries;
      this.currentPage= this.pageService.markFirstPage();
    }));
  }

  getDiscoveries(): void {
    this.discoveryService.getDiscoveries().subscribe(discoveries => this.discoveries = discoveries);
  }

  addVote(voteType, id):void {
    window.location.href = 'http://localhost:8080/projekt/api/vote?vote=' + voteType + '&discoveryId=' + id;
    setTimeout(() =>
    this.getDiscoveries(), 500);

  }

  showAllDiscoveries(){
    let pages=document.getElementById("pages");
    let button=document.getElementById("showAllDiscoveries");
    if(button.style.opacity==="1"){
      button.style.opacity="0.5";
      this.givePreparedPagesDependsOfElementsOnOnePage(5);
      pages.style.visibility="visible";
    }else {
      button.style.opacity = "1";
      this.getDiscoveries();
      pages.style.visibility="hidden";
    }

  }
  showFiveElements(numberOfFirstElement:number){
    for(let element of this.htmlCollectionAsArray.slice(numberOfFirstElement-1,numberOfFirstElement+5)){
      element.style.display="";
    }
    this.firstIndexOfHidden+=5;
  }

}
interface UiEvent{
  pageY:number;
}
