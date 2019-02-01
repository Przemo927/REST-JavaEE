import { Component, OnInit } from '@angular/core';
import { DiscoveryService } from '../service/discovery.service';
import { Discovery } from '../model/discovery';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';



@Component({
  selector: 'app-editdiscovery',
  templateUrl: './editdiscovery.component.html',
  styleUrls: ['./editdiscovery.component.css']
})
export class EditdiscoveryComponent implements OnInit {

  discovery: Discovery;
  constructor(private discoveryService: DiscoveryService,private route: ActivatedRoute, private location: Location) { }
  
  updateDiscovery(): void {
  this.discoveryService.updateDiscovery(this.discovery)
  .subscribe(()=> this.goBack());
  }
  
  
  getDiscovery(): void {
  let id:number = <number><any>this.route.snapshot.paramMap.get('id');
  this.discoveryService.getDiscovery(id).subscribe(discovery => this.discovery = discovery);
  }
  
  goBack(): void {
  this.location.back();
  }
  
  deleteDiscovery() {
  let id:number=<number><any>this.route.snapshot.paramMap.get('id');
  this.discoveryService.deleteDiscovery(id).subscribe(()=>this.goBack());
  }

  ngOnInit() {
  this.getDiscovery();
  }

}
