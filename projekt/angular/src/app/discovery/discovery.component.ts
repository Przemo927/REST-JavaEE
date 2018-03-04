import { Component, OnInit } from '@angular/core';
import { DiscoveryService } from '../discovery.service';
import { CommentService } from '../comment.service';
import { Discovery } from '../discovery';
import { ActivatedRoute } from '@angular/router';
import { Comment } from '../comment';

@Component({
  selector: 'app-discovery',
  templateUrl: './discovery.component.html',
  styleUrls: ['./discovery.component.css']
})
export class DiscoveryComponent implements OnInit {

  discovery: Discovery;
  comments: Comment[]=[];
  comment: Comment=new Comment();
  VOTE_UP='VOTE_UP';
  VOTE_DOWN='VOTE_DOWN';

  constructor(private discoveryService: DiscoveryService,private route: ActivatedRoute, private commentService: CommentService) {}

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
    this.commentService.addComment(this.comment,id).subscribe(comment=>
      this.getCommentsById());
  }

  addVote(voteType,id){
    window.location.href='http://localhost:8080/projekt/api/votecom?vote='+voteType+'&commentId='+id;
    setTimeout(()=>
      this.getCommentsById(),500);
  }

  ngOnInit() {
    this.getDiscovery();
    this.getCommentsById();
  }

}
