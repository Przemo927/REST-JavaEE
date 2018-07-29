import {Component, OnInit, ViewChild} from '@angular/core';
import { DiscoveryService } from '../discovery.service';
import { Discovery } from '../discovery';
import * as url from "url";
import {ValidatabletextareaComponent} from "../validatabletextarea/validatabletextarea.component";
import {ValidatableinputComponent} from "../validatableinput/validatableinput.component";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-adddiscovery',
  templateUrl: './adddiscovery.component.html',
  styleUrls: ['./adddiscovery.component.css']
})
export class AdddiscoveryComponent implements OnInit {
  @ViewChild('description') descriptionComponent: ValidatabletextareaComponent;
  @ViewChild('url') urlComponent:ValidatableinputComponent;
  @ViewChild('name') nameComponent:ValidatableinputComponent;


  private discovery= new Discovery();
  private invalidFieldList: InvalidFieldList= {};


  constructor(private discoveryService: DiscoveryService) { }

  ngOnInit() {
  }

  addDiscovery(discovery: Discovery) {
    this.discoveryService.addDiscovery(discovery).subscribe((res) => {
    if (res!==undefined && res.InvalidFieldList != null){
        this.invalidFieldList = res.InvalidFieldList;
        this.checkInvalidFieldList(this.invalidFieldList);
    }
  });

}
  assignDescription(description: string){
    this.discovery.description=description;
  }
  assignUrl(url:string){
    this.discovery.url=url;
  }
  assignName(name:string){
    this.discovery.name=name;
  }

  checkInvalidFieldList(invalidFieldList: InvalidFieldList){
    if(invalidFieldList.name!==undefined){
      this.nameComponent.setWrong();
      this.nameComponent.setValidationMessage(invalidFieldList.name);
    }
    if(invalidFieldList.url!==undefined){
      this.urlComponent.setWrong();
      this.urlComponent.setValidationMessage(invalidFieldList.url);
    }
    if(invalidFieldList.description!==undefined){
      this.descriptionComponent.setWrong();
      this.descriptionComponent.setValidationMessage(invalidFieldList.description);
    }
  }
}

interface InvalidFieldList {
    url?: string;
    name?: string;
    description?: string;
}
