import {Component, DoCheck, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {NameofcityService} from "../nameofcity.service";
import {Event} from "../event";
import {EventService} from "../event.service";
import * as L from "leaflet";
import {EventPosition} from "../eventposition";
import {DataService} from "../data.service";


@Component({
  selector: 'app-listofevents',
  templateUrl: './listofevents.component.html',
  styleUrls: ['./listofevents.component.css']
})
export class ListofeventsComponent implements OnInit, DoCheck {

  private events:Event[];
  private monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December"];


  constructor(private nameService:NameofcityService, private eventService:EventService,private router:Router,private dataService:DataService) {
  }

  ngOnInit() {
    if(this.router.url.indexOf('searchByCity')!==-1 && this.nameService.observable!=null){
      this.nameService.observable.subscribe((x) => {
        console.log(x.target.value);
        this.getEventByName(x.target.value);
      });
    }
    else if(this.router.url.indexOf('searchByPosition')!==-1){
      this.dataService.currentData.subscribe((events)=>this.events=events);
    }
    else{
      this.getEvents();
    }

  }
  ngDoCheck(){
  }


  private getEventByName(nameOfCity:string){
    this.eventService.getEventsByNameOfCity(nameOfCity).subscribe((events)=>{
        this.events=events;
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
