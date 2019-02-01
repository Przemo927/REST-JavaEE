import {AfterViewInit, Component, OnInit} from '@angular/core';
import * as L from 'leaflet';
import { EventService } from '../service/event.service';
import { Event } from '../event';
import {EventPosition } from '../eventposition';

@Component({
  selector: 'app-addevent',
  templateUrl: './addevent.component.html',
  styleUrls: ['./addevent.component.css']
})
export class AddeventComponent implements OnInit, AfterViewInit {

  private myMap= null;
  private markers= [];
  private displayMap: any= {'display': 'none'};
  private inputDateTimePicker: any;
  private inputTimePicker: any;

   private event= new Object();

  constructor(private eventService: EventService) {

  }
  ngAfterViewInit(){
    this.inputDateTimePicker = document.getElementsByTagName('p-calendar')[0].getElementsByTagName('input')[0];
    this.inputTimePicker = document.getElementsByTagName('p-calendar')[1].getElementsByTagName('input')[0];
    this.inputTimePicker.classList.remove("ui-corner-all");
    this.inputTimePicker.classList.add('form-control');
    this.inputTimePicker.parentElement.style.width = '49%';
    this.inputTimePicker.parentElement.style.float = 'right';
    this.inputDateTimePicker.classList.remove("ui-corner-all");
    this.inputDateTimePicker.classList.add('form-control');
    this.inputDateTimePicker.parentElement.style.width = '49%';
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
   this.displayMap = { 'margin-top' : '1em', 'padding-bottom' : '1em' };
 }


addEvent(event: Event): void {
  event.timestamp = new Date(this.inputDateTimePicker.value + ' ' + this.inputTimePicker.value);
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
  console.log(event);
}



}
