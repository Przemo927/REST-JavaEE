import {Component, DoCheck, OnInit} from "@angular/core";
import {Router, NavigationEnd} from "@angular/router";
import {NameofcityService} from "../nameofcity.service";
import {Event} from "../event";
import {EventService} from "../event.service";
import * as L from "leaflet";
import {EventPosition} from "../eventposition";
import {DataService} from "../data.service";
import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import { User } from "../user";


@Component({
  selector: 'app-listofevents',
  templateUrl: './listofevents.component.html',
  styleUrls: ['./listofevents.component.css']
})
export class ListofeventsComponent implements OnInit, DoCheck {

  private events:Event[]=[];
  private monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December"];


  constructor(private nameService:NameofcityService, private eventService:EventService,private router:Router,private dataService:DataService) {
  }

  ngOnInit() {
    this.router.events.forEach((e) => {
      if (e instanceof NavigationEnd) {
        console.log("**************");
        console.log(e.url);
        console.log("**************");
      }
    });
    if(this.router.url.indexOf('searchByCity')!==-1 && this.nameService.observable!=null){
      this.nameService.observable.map((event)=>event=event.target.value).debounceTime(500)
        .distinctUntilChanged()
        .subscribe((value) => {
        this.getEventByName(value);
      });
    }
    else if(this.router.url.indexOf('searchByPosition')!==-1){
      this.dataService.currentData.debounceTime(1000).distinctUntilChanged().subscribe((events)=>this.events=events);
      this.getEvents();
    }
    else{
      this.getEvents();
    }

  }
  ngDoCheck(){
  }


  private getEventByName(nameOfCity:string){
    this.eventService.getEventsByNameOfCity(nameOfCity).subscribe((events) => {
        this.getEvents();
        //this.events=events;
      }
    )
  }
  private getEvents() {
    for (let i = 0; i < 10; i++) {
      let event = new Event();
      event.id = i;
      event.nameOfCity = "Kraków" + i;
      event.timestamp = new Date();
      event.eventPosition = [new EventPosition(50.04999+i/100, 19.93696-i/100)];
      event.user = new User();
      this.events.push(event);
    }
    //return this.eventService.getEvents().subscribe(events => this.events=events);
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
