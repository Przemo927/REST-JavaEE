import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs/Observable";
import { EventService } from '../event.service';
import{ Event } from '../event';
import { ActivatedRoute,Router } from '@angular/router';
import { NameofcityService } from '../nameofcity.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-searcheventbycity',
  templateUrl: './searcheventbycity.component.html',
  styleUrls: ['./searcheventbycity.component.css']
})
export class SearcheventbycityComponent implements OnInit {
  private events:Event[];
  private napis:string="";

  constructor(private eventService: EventService,private router: Router,private route: ActivatedRoute,private nameService:NameofcityService
    ,private dataService:DataService) { }

  ngOnInit() {
    this.dataService.addBehaviourSource(this.events);
    let searchInput=<HTMLInputElement>document.getElementsByName("nameOfCity")[0];
    console.log(searchInput);
    let input$=Observable.fromEvent(searchInput,'keyup');
    this.nameService.observable=input$;

  }

  public sendNameToComponent(nameOfCity:string){
    console.log('Przemek');
    this.router.navigate([{outlets: {primary: 'searchByCity', events: nameOfCity}}],
      {relativeTo: this.route,skipLocationChange: true});
  }

}
