import { Component, OnInit } from '@angular/core';
import { EventService } from '../event.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  constructor(private eventService: EventService) { }

  private events;


ngOnInit() {

}

getEventsByNameOfCity(nameOfCity:string){
    return this.eventService.getEventsByNameOfCity(nameOfCity).subscribe(events=>this.events=events);
}

}

