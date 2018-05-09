import {Component, OnInit} from "@angular/core";
import {EventService} from "../event.service";
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


ngOnInit() {
  this.positionStyle={'opacity':'0.5'};
  this.cityStyle={'opacity':'0.5'};
  this.allStyle={'opacity':'1'};
}
  searchByPosition():void {
    this.positionStyle={'opacity':'1'};
    this.cityStyle={'opacity':'0.5'};
    this.allStyle={'opacity':'0.5'};
  }
  searchByCity():void {
    this.positionStyle={'opacity':'0.5'};
    this.cityStyle={'opacity':'1'};
    this.allStyle={'opacity':'0.5'};
  }
  showAllEvents():void {
    this.positionStyle={'opacity':'0.5'};
    this.cityStyle={'opacity':'0.5'};
    this.allStyle={'opacity':'1'};
  }

getEventsByNameOfCity(nameOfCity:string){
    return this.eventService.getEventsByNameOfCity(nameOfCity).subscribe(events=>this.events=events);
}

}

