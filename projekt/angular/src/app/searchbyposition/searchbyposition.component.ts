import { Component, OnInit, IterableDiffers, IterableDiffer, DoCheck } from '@angular/core';
import { Event } from '../event';
import * as L from 'leaflet';
import { EventService } from '../event.service';
import { DataService } from '../data.service';
import { Observable } from "rxjs/Observable";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

@Component({
  selector: 'app-searchbyposition',
  templateUrl: './searchbyposition.component.html',
  styleUrls: ['./searchbyposition.component.css']
})
export class SearchbypositionComponent implements OnInit,DoCheck {

  private events:Event[];
  private myMap;
  private markers=[];
  private iterableDiffer: IterableDiffer<any>;
  private distance:number=0;


  ngDoCheck(){
    let change=this.iterableDiffer.diff(this.markers);
    if(change!=null){
      if(this.markers.length!==0){
        if(this.distance==null || !SearchbypositionComponent.validateDistance(this.distance.toString())){
          this.distance=0;
        }
        this.getEventsByPosition(this.distance);
      }
    }
  }
  ngOnInit() {
    this.dataService.addBehaviourSource(this.events);
    let distanceInput=document.getElementsByName("distance");

    Observable.fromEvent(distanceInput,'keyup').subscribe(
      ()=>{
        if(this.markers!==undefined && this.markers.length>0) {
          this.getEventsByPosition(this.distance);
        }
      }
    );

  }
  constructor(private eventService:EventService, private iterableDiffers:IterableDiffers, private dataService:DataService<Event[]>) {
    this.iterableDiffer=iterableDiffers.find([]).create(null);
  }

  static validateDistance(value:string):boolean{
    let regExp=new RegExp("[1-9]{1,4}");
    return regExp.test(value);
  }
  getEventsByPosition(distance:number){
    let latitude=this.markers[0]._latlng.lat;
    let longtitude=this.markers[0]._latlng.lng;
    return this.eventService.getEventsByPosition(latitude,longtitude,distance).subscribe(events => {
      this.events=events;
      this.dataService.changeData(events);
    });
  }

  addLayerToMap(){
    if(this.myMap==undefined){
      this.myMap=L.map('findByPosition',{center: [52.00, 19.00],zoom: 5});
      L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' }).addTo(this.myMap);
      this.myMap.on('click',(e)=> {
        this.addMarker(e);
      });

      this.myMap.on('contextmenu',(e)=>{
        this.removeMarker(e);
      });
    }

  }

  addMarker(e){
    if(this.markers.length<1){
      let marker = L.marker(e.latlng, {
        icon: L.icon({
          iconSize: [25, 41],
          iconAnchor: [13, 41],
          iconUrl: 'assets/marker-icon.png',
          shadowUrl: 'assets/marker-shadow.png'
        })
      }).on('contextmenu', (e) => {
        this.removeMarker(e)
      });

      this.markers.push(marker);
      this.myMap.addLayer(marker);
    }

  }

  removeMarker(e){
    for (let marker of this.markers){
      if(e.latlng==marker.getLatLng()){
        this.myMap.removeLayer(marker);
        let idOfMarker=this.markers.indexOf(marker);
        this.markers.splice(idOfMarker,1);
      }
    }
  }
  private maksimumValue(){
    if(this.distance!==null && this.distance.toString().length>3){
      this.distance=Number(this.distance.toString().slice(0,3));
    }
  }

}
