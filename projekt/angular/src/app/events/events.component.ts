import {Component, OnInit} from "@angular/core";
import {EventService} from "../service/event.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.css']
})
export class EventsComponent implements OnInit {

  constructor(private eventService: EventService,private router:Router) { }

  private events;
  private cityStyle;
  private positionStyle;
  private allStyle;
  private title:string;


ngOnInit() {
  this.positionStyle={'opacity':'1'};
  this.cityStyle={'opacity':'0.5'};
  this.allStyle={'opacity':'0.5'};
  this.title='Search events By Positon';
}
  searchByPosition():void {
    this.positionStyle={'opacity':'1'};
    this.cityStyle={'opacity':'0.5'};
    this.allStyle={'opacity':'0.5'};
    this.title='Search events By Positon';
  }
  searchByCity():void {
    this.positionStyle={'opacity':'0.5'};
    this.cityStyle={'opacity':'1'};
    this.allStyle={'opacity':'0.5'};
    this.title='Search events by city';
  }
  showAllEvents():void {
    this.positionStyle={'opacity':'0.5'};
    this.cityStyle={'opacity':'0.5'};
    this.allStyle={'opacity':'1'};
    this.title='All events';
  }

getEventsByNameOfCity(nameOfCity:string){
    return this.eventService.getEventsByNameOfCity(nameOfCity).subscribe(events=>this.events=events);
}

}

