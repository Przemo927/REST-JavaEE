import {Component, DoCheck, IterableDiffers} from "@angular/core";
import {DiscoveryService} from "../service/discovery.service";
import {CheckUserService} from "../service/check-user.service";
import {DataService} from "../service/data.service";
import {DiscoveriesComponent} from "./discoveries.component";
import {KeyService} from "../service/key.service";

@Component({
  selector: 'app-encrypted-discoveries',
  templateUrl: './discoveries.component.html',
  styleUrls: ['./discoveries.component.css']
})

export class DiscoveriesEncryptedComponent extends DiscoveriesComponent implements DoCheck{

  private encrypt=true;
  constructor(discoveryService: DiscoveryService, checkUserService: CheckUserService, iterableDiffers:IterableDiffers,
               dataService:DataService,private keyService:KeyService) {
    super(discoveryService,checkUserService,iterableDiffers,dataService);
  }

  ngDoCheck() {
    if(this.dataService.currentData!==undefined) {
      this.dataService.currentData.subscribe((discoveries) =>{
        this.discoveries = discoveries
      });
    }
  }
}
