import {Component, DoCheck, IterableDiffer, IterableDiffers, OnDestroy, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {NameofcityService} from "../service/nameofcity.service";
import {Event} from "../model/event";
import {EventService} from "../service/event.service";
import * as L from "leaflet";
import {EventPosition} from "../model/eventposition";
import {DataService} from "../service/data.service";
import "rxjs/add/operator/map";


@Component({
  selector: 'app-listofevents',
  templateUrl: './listofevents.component.html',
  styleUrls: ['./listofevents.component.css']
})
export class ListofeventsComponent implements OnInit,DoCheck, OnDestroy {

  private events:Event[];
  private isSerachByPositionUrl:boolean;
  private paginationVisibility:boolean;
  private isSearchByCityUrl:boolean;
  private isAllEventsUrl:boolean;
  private monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December"];
  private iterableDiffer: IterableDiffer<any>;
  private autoGenerate:string;


  constructor(private nameService:NameofcityService, private eventService:EventService,private router:Router,private dataService:DataService, private iterableDiffers:IterableDiffers) {
    this.iterableDiffer=iterableDiffers.find([]).create(null);
  }
  ngDoCheck() {
    if(this.dataService.currentData!==undefined && !this.isSearchByCityUrl && !this.isSerachByPositionUrl) {
      this.dataService.currentData.subscribe((events) => {
        this.convertFromTimeStampToDate(events);
        this.events = events;
      });
    }
  }

  private convertFromTimeStampToDate(events:Event[]){
    if(events!==null && events!==undefined) {
      for (let event of events) {
        event.timestamp = new Date(event.timestamp);
      }
    }
  }

  ngOnInit() {
    this.dataService.addBehaviourSource([]);
    this.isSerachByPositionUrl=this.router.url.indexOf('searchByPosition')!==-1;
    this.isSearchByCityUrl=this.router.url.indexOf('searchByCity')!==-1;
    this.isAllEventsUrl=this.router.url.indexOf('listOfAllEvents')!==-1;
    this.setListOfEventsDepndsOfUrl();
  }

  private setListOfEventsDepndsOfUrl(){
    if(this.isSearchByCityUrl && this.nameService.observable!=null){
      this.paginationVisibility=false;
      this.nameService.observable.map((event)=> {
        return event.target.value;
      }).debounceTime(500)
        .distinctUntilChanged()
        .subscribe((value) => {
          this.getEventByName(value);
        });
    }
    else if(this.isSerachByPositionUrl){
      this.paginationVisibility=false;
    }
    else if(this.isAllEventsUrl){
      this.paginationVisibility=true;
      this.autoGenerate='true';
    }
  }
  ngOnDestroy(){
    this.dataService.changeData(null);
  }
  private getEventByName(nameOfCity:string){
    this.eventService.getEventsByNameOfCity(nameOfCity).subscribe((events) => {
        this.getEvents();
      }
    )
  }
  private getEvents() {
    return this.eventService.getEvents().subscribe(events => this.events=events);
  }

  addOptionsToMap(eventPositions:EventPosition[]) {
    let layers=[];
    layers.push(L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' }));
    for(let eventPosition of eventPositions){
      layers.push(L.marker({lat:eventPosition.xCoordinate,lng:eventPosition.yCoordinate}, {
        icon: L.icon({
          iconSize: [ 25, 41 ],
          iconAnchor: [ 13, 41 ],
          iconUrl: 'assets/marker-icon.png',
          shadowUrl: 'assets/marker-shadow.png'
        })
      }));
    }

    let options = {
      layers: layers,
      zoom: 10,
      center: L.latLng([ eventPositions[0].xCoordinate,eventPositions[0].yCoordinate ]),
      zoomControl: false,
    };
    return options;
  }

}
