import {Component, ContentChild, ElementRef, OnInit, Output, ViewChild} from "@angular/core";
import {DiscoveryService} from "../discovery.service";
import {CommentService} from "../comment.service";
import {Discovery} from "../discovery";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {Comment} from "../comment";
import {ValidatabletextareaComponent} from "../validatabletextarea/validatabletextarea.component";
import {UrlUtils} from "../UrlUtils";

@Component({
  selector: 'app-discovery',
  templateUrl: './discovery.component.html',
  styleUrls: ['./discovery.component.css'],
})
export class DiscoveryComponent implements OnInit {

  @ViewChild(ValidatabletextareaComponent) messageRef: ValidatabletextareaComponent;

  discovery: Discovery;
  comments: Comment[]=[];
  comment: Comment=new Comment();
  VOTE_UP='VOTE_UP';
  VOTE_DOWN='VOTE_DOWN';
  message:string='';
  id:number;

  constructor(private discoveryService: DiscoveryService,private route: ActivatedRoute,
              private commentService: CommentService, private router:Router) {
    router.events.forEach((e) => {
      if(e instanceof NavigationEnd){
        this.id = +this.route.snapshot.paramMap.get('id');
        this.getDiscovery();
        this.getCommentsById();
      }
    });
  }

  ngOnInit() {
    this.id = +this.route.snapshot.paramMap.get('id');
    this.getDiscovery();
    this.getCommentsById();
  }

  getDiscovery(): void {
    this.discoveryService.getDiscovery(this.id).subscribe(discovery=>this.discovery=discovery);
  }

  getCommentsById(): void {
    this.commentService.getCommentsById(this.id).subscribe(comments=> this.comments=comments);
  }

  addComment():void {
    this.commentService.addComment(this.comment,this.id).subscribe(response=>{
      if(response!==null && response.InvalidFieldList!==undefined){
        this.message=response.InvalidFieldList.comment;
        this.messageRef.setWrong();
        this.messageRef.setValidationMessage(this.message);
      }else {
        this.getCommentsById();
      }
    });
  }

  addVote(voteType,id){
    let urlAddVote=UrlUtils.generateBasicUrl(window.location.protocol);
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'vote',voteType);
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'commentId',id);
    console.log(urlAddVote);
    window.location.href='http://localhost:8080/projekt/api/votecom?vote='+voteType+'&commentId='+id;
    setTimeout(()=>
      this.getCommentsById(),500);
  }

  assignComment(comment:string){
    this.comment.comment=comment;
  }

}
