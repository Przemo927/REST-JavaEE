import {Component, OnInit, ViewChild} from '@angular/core';
import {DiscoveryService} from '../service/discovery.service';
import {Discovery} from '../model/discovery';
import {ValidatabletextareaComponent} from "../validatabletextarea/validatabletextarea.component";
import {ValidatableinputComponent} from "../validatableinput/validatableinput.component";

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

  constructor(private discoveryService: DiscoveryService) { }

  ngOnInit() {
  }

  addDiscovery(discovery: Discovery) {
    this.discoveryService.addDiscovery(discovery).subscribe((res) => {
      if (res!==undefined && res.invalidFieldList != null){
          this.checkInvalidFieldList(res.invalidFieldList);
      }else{
        this.nameComponent.setWrong();
        this.urlComponent.setWrong();
        this.descriptionComponent.setWrong();
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
