import {Component, ContentChild, ElementRef, OnInit, Output, ViewChild} from "@angular/core";
import {DiscoveryService} from "../discovery.service";
import {CommentService} from "../comment.service";
import {Discovery} from "../discovery";
import {ActivatedRoute} from "@angular/router";
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
  message:string;

  constructor(private discoveryService: DiscoveryService,private route: ActivatedRoute,
              private commentService: CommentService) {}


  getDiscovery(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.discoveryService.getDiscovery(id).subscribe(discovery=>this.discovery=discovery);
  }

  getCommentsById(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commentService.getCommentsById(id).subscribe(comments=> this.comments=comments);
  }

  addComment():void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commentService.addComment(this.comment,id).subscribe(response=>{
      this.message='';
        this.getCommentsById();
        let textArea=document.getElementsByName("comment")[0];
        textArea.classList.remove("highlight");
        if(response!==null){
          //need to reload keyframes
          void textArea.offsetWidth;

          if(response.InvalidFieldList!==undefined){
            this.message=response.InvalidFieldList.comment;
            this.messageRef.setWrong();
            this.messageRef.setValidationMessage(this.message);
          }
        }
    }
      );
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

  ngOnInit() {
    let urlAddVote=UrlUtils.generateBasicUrl(window.location.protocol);
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'vote','VOTE_UP');
    urlAddVote=UrlUtils.addParameterToUrl(urlAddVote,'commentId',1);
    console.log(urlAddVote);
    this.getDiscovery();
    this.getCommentsById();
  }

  assignComment(comment:string){
    this.comment.comment=comment;
  }

}
