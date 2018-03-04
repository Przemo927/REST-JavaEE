import { Component, OnInit } from '@angular/core';
import { DiscoveryService } from '../discovery.service';
import { Discovery } from '../discovery';
import { CheckUserService } from '../check-user.service';
import { VoteService } from '../vote.service';

@Component({
  selector: 'app-discoveries',
  templateUrl: './discoveries.component.html',
  styleUrls: ['./discoveries.component.css']
})
export class DiscoveriesComponent implements OnInit {


  constructor(private discoveryService: DiscoveryService, private checkUserService: CheckUserService, private voteService: VoteService) { }

  discoveries: Discovery[]=[];
  private adminRole=false;
  private VOTE_UP='VOTE_UP';
  private VOTE_DOWN='VOTE_DOWN';


  getDiscoveries(): void {
    this.discoveryService.getDiscoveries().subscribe(discoveries => this.discoveries = discoveries);
  }

  ngOnInit() {
    this.getDiscoveries();
    this.checkUserService.getUserInformation().subscribe(
      e=>{
        if(e['role']=='admin'){
          this.adminRole=true;
        }

      });
  }

  addVote(voteType,id){
    window.location.href ="http://localhost:8080/projekt/api/vote?vote="+voteType+"&discoveryId="+id;
    setTimeout(()=>
      this.getDiscoveries(),500);

  }


}
