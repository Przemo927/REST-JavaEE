import { Component, OnInit } from '@angular/core';
import { DiscoveryService } from '../discovery.service';
import { Discovery } from '../discovery';

@Component({
  selector: 'app-adddiscovery',
  templateUrl: './adddiscovery.component.html',
  styleUrls: ['./adddiscovery.component.css']
})
export class AdddiscoveryComponent implements OnInit {

  private discovery= new Object();
  private invalidFieldList: InvalidFieldList= {};


  constructor(private discoveryService: DiscoveryService) { }

  ngOnInit() {
  }


  addDiscovery(discovery: Discovery) {
    this.discoveryService.addDiscovery(discovery).subscribe((res) => {
    if (res.InvalidFieldList != null){
        this.invalidFieldList = res.InvalidFieldList;
    }
  });

}
}

interface InvalidFieldList {
    url?: string;
    name?: string;
    description?: string;
}
