import { Component, OnInit } from '@angular/core';
import {DraggableService} from "../draggable.service";
import {DraggableElement} from "../draggableelement";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  private draggableDiv1:DraggableElement;
  private draggableDiv2:DraggableElement;
  constructor() {}

  ngOnInit() {
    this.draggableDiv1=DraggableService.prepareDraggableElement(document.querySelector("#mainMenu"),document.querySelector("#div"));
    this.draggableDiv2=DraggableService.prepareDraggableElement(document.querySelector("#mainMenu"),document.querySelector("#div1"));
  }
}
