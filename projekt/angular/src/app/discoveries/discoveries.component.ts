///<reference path="../check-user.service.ts"/>
import {Component, DoCheck, IterableDiffer, IterableDiffers, OnInit} from "@angular/core";
import {DiscoveryService} from "../discovery.service";
import {Discovery} from "../discovery";
import {CheckUserService} from "../check-user.service";
import {Page} from "../page";
import {Observable} from "rxjs/Rx";
import {DataService} from "../data.service";
import {DiscoveryPageService} from "../discoverypage.service";

@Component({
  selector: 'app-discoveries',
  templateUrl: './discoveries.component.html',
  styleUrls: ['./discoveries.component.css']
})
export class DiscoveriesComponent implements OnInit, DoCheck {


constructor(private discoveryService: DiscoveryService, private checkUserService: CheckUserService, private iterableDiffers:IterableDiffers,
            private dataService:DataService) {
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
  }

  getDiscoveries(): void {
    this.discoveryService.getDiscoveries().subscribe(discoveries => this.discoveries = discoveries);
  }

  addVote(voteType, id):void {
    window.location.href = 'http://localhost:8080/projekt/api/vote?vote=' + voteType + '&discoveryId=' + id;
    setTimeout(() =>
    this.getDiscoveries(), 500);

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
