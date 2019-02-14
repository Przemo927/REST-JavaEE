import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs/Observable";
import { EventService } from '../service/event.service';
import{ Event } from '../model/event';
import { ActivatedRoute,Router } from '@angular/router';
import { NameofcityService } from '../service/nameofcity.service';
import { DataService } from '../service/data.service';

@Component({
  selector: 'app-searcheventbycity',
  templateUrl: './searcheventbycity.component.html',
  styleUrls: ['./searcheventbycity.component.css']
})
export class SearcheventbycityComponent implements OnInit {

  constructor(private nameService:NameofcityService) { }

  ngOnInit() {
    let searchInput=<HTMLInputElement>document.getElementsByName("nameOfCity")[0];
    let input$=Observable.fromEvent(searchInput,'keyup');
    this.nameService.observable=input$;
  }

}
