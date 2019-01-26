import {Component, OnDestroy, OnInit, ViewChild} from "@angular/core";
import {DiscoveryService} from "../discovery.service";
import {CommentService} from "../comment.service";
import {Discovery} from "../discovery";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Comment} from "../comment";
import {ValidatabletextareaComponent} from "../validatabletextarea/validatabletextarea.component";
import {UrlUtils} from "../UrlUtils";
import {EndPoint} from "../endpoint.enum";
import {UrlRegex} from "../url-regex.enum";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-discovery',
  templateUrl: './discovery.component.html',
  styleUrls: ['./discovery.component.css'],
})
export class DiscoveryComponent implements OnInit, OnDestroy {

  @ViewChild(ValidatabletextareaComponent) messageRef: ValidatabletextareaComponent;

  discovery: Discovery;
  comments: Comment[]=[];
  comment: Comment=new Comment();
  VOTE_UP='VOTE_UP';
  VOTE_DOWN='VOTE_DOWN';
  message:string='';
  id:number;
  routerEvent:Subscription;

  constructor(private discoveryService: DiscoveryService,private route: ActivatedRoute,
              private commentService: CommentService, private router:Router) {
    this.routerEvent=router.events.subscribe((e) => {
      if(e instanceof NavigationEnd && e.url.match(UrlRegex.discoveryWithIdUrl) !== null){
        this.id = +this.route.snapshot.paramMap.get('id');
        this.getDiscovery();
      }
    });
  }

  ngOnInit() {
  }

  getDiscovery(): void {
    this.discoveryService.getDiscovery(this.id).subscribe(discovery=>{
      this.discovery=discovery;
      this.comments=discovery.comments;
    });
  }

  addComment():void {
    this.comment.discovery=this.discovery;
    this.commentService.addComment(this.comment).subscribe(response=>{
      if(response!==null && response.invalidFieldList!==undefined){
        this.message=response.invalidFieldList.comment;
        this.messageRef.setWrong();
        this.messageRef.setValidationMessage(this.message);
      }else {
        this.getDiscovery();
      }
    });
  }

  addVote(voteType,id){
    let urlAddVote=UrlUtils.generateBasicUrl(window.location)+EndPoint.voteComment;
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'vote',voteType);
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'commentId',id);
    window.location.href=urlAddVote;
    setTimeout(()=>
      this.getDiscovery(),500);
  }

  assignComment(comment:string){
    this.comment.comment=comment;
  }

  ngOnDestroy(): void {
    this.routerEvent.unsubscribe();
  }

}
