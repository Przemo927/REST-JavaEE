import { Component, OnInit } from '@angular/core';
import { Discovery } from '../model/discovery';
import {DiscoveryService} from "../service/discovery.service";

@Component({
  selector: 'app-leftmenu',
  templateUrl: './leftmenu.component.html',
  styleUrls: ['./leftmenu.component.css']
})
export class LeftmenuComponent implements OnInit {

  private discoveries: Discovery[]=[];

  constructor(private discoveryService:DiscoveryService) { }

  ngOnInit() {
    this.discoveryService.getDiscoveriesWithLimitAndOrder(0,5,'date').subscribe((discoveries)=>{
      this.discoveries=discoveries;
    });
  }

}
