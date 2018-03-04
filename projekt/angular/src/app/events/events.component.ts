import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';
import { Event } from '../event';
import { EventPosition } from '../eventposition';
import * as L from 'leaflet';
 
@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  constructor(private eventService: EventService) { }
   
  private city=false;
  private events;
  private options;
  private myMap;
  private position=true;
  private monthNames = ["January", "February", "March", "April", "May", "June","July", "August", "September", "October", "November", "December"];
  private showMap=true;
  private markers=[];

  
ngOnInit() {
}

enableFindByPosition(){
    this.events=null;
    this.city=false;
    this.position=true;
    this.myMap=undefined;
    this.markers=[];
    
}

getEventsByPosition(distance:number){
    let latitude=this.markers[0]._latlng.lat;
    let longtitude=this.markers[0]._latlng.lng;
    return this.eventService.getEventsByPosition(latitude,longtitude,distance).subscribe(events => this.events=events);
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
    var marker=L.marker(e.latlng, {
        icon: L.icon({
        iconSize: [ 25, 41 ],
        iconAnchor: [ 13, 41 ],
        iconUrl: 'assets/marker-icon.png',
        shadowUrl: 'assets/marker-shadow.png'
        })
    }).on('contextmenu',(e)=>{this.removeMarker(e)});

    this.markers.push(marker);
    this.myMap.addLayer(marker);
    
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




enableFindByCity() {
    this.events=null;
    this.city=true;
    this.position=false;
}

getEventsByNameOfCity(nameOfCity:string){
    return this.eventService.getEventsByNameOfCity(nameOfCity).subscribe(events=>this.events=events);
}






enableShowAllEvents(){
    this.position=false;
    this.city=false;
    this.getEvents();
}

getEvents() {
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

