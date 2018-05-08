import { Component, OnInit } from '@angular/core';
import * as L from 'leaflet';
import { EventService } from '../event.service';
import { Event } from '../event';
import {EventPosition } from '../eventposition';

@Component({
  selector: 'app-addevent',
  templateUrl: './addevent.component.html',
  styleUrls: ['./addevent.component.css']
})
export class AddeventComponent implements OnInit {

  private myMap= null;
  private markers= [];
  private displayMap: any= {'display': 'none'};
  private formClass= 'col-md-6 col-md-offset-3';

   private event= new Object();

  constructor(private eventService: EventService) {

  }

  ngOnInit() {
    this.myMap = L.map('mapa', {center: [52.00, 19.00], zoom: 5});
    L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 18, attribution: '...' }).addTo(this.myMap);

    this.myMap.on('click', (e) => {
    this.addMarker(e);
    });

    this.myMap.on('contextmenu', (e) => {
    this.removeMarker(e);
    });

  }



 removeMarker(e){
    for (const marker of this.markers){
        if (e.latlng == marker.getLatLng()){
            this.myMap.removeLayer(marker);
            const idOfMarker = this.markers.indexOf(marker);
            this.markers.splice(idOfMarker, 1);
        }
    }
 }

 addMarker(e){
    const marker = L.marker(e.latlng, {
        icon: L.icon({
        iconSize: [ 25, 41 ],
        iconAnchor: [ 13, 41 ],
        iconUrl: 'assets/marker-icon.png',
        shadowUrl: 'assets/marker-shadow.png'
        })
    }).on('contextmenu', (e) => {this.removeMarker(e); });

    this.markers.push(marker);
    this.myMap.addLayer(marker);
 }

 showMap(){
    const titlePosition = document.getElementById('title').offsetTop;
    this.displayMap = {'margin-top': titlePosition};
    this.formClass = 'col-md-6';
 }


addEvent(event: Event, date: string, time: string): void {
  event.timestamp = new Date(date + ' ' + time);
  event.eventPosition = new Array();
  if (this.markers.length == 0){
    this.showMap();
  }
  else{
      for (const marker of this.markers){
        event.eventPosition.push(new EventPosition(marker._latlng.lat, marker._latlng.lng));
      }
      this.eventService.addEvent(event).subscribe();
      /*.subscribe(()=> this.goBack());*/
  }
  }



}
