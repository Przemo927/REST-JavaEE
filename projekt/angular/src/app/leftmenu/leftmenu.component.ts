import { Component, OnInit } from '@angular/core';
import { Discovery } from '../discovery';

@Component({
  selector: 'app-leftmenu',
  templateUrl: './leftmenu.component.html',
  styleUrls: ['./leftmenu.component.css']
})
export class LeftmenuComponent implements OnInit {

  private discoveries: Discovery[]=[];

  constructor() { }

  ngOnInit() {
    let i = 0;
    for (i = 0; i < 5; i++) {
      let disc = new Discovery();
      disc.id = i;
      disc.url = 'http://url' + i + '.pl';
      disc.description = 'spoko spoko spoko' + i;
      disc.name = 'Name name name' + i;
      this.discoveries.push(disc);
    }
  }

}
