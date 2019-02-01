import {Component, OnInit} from "@angular/core";
import {User} from "../model/user";
import {UserService} from "../service/user.service";
import {ActivatedRoute} from "@angular/router";
import {Location} from "@angular/common";


@Component({
  selector: 'app-edituser',
  templateUrl: './edituser.component.html',
  styleUrls: ['./edituser.component.css']
})
export class EdituserComponent implements OnInit {

  constructor(private userService: UserService,private route: ActivatedRoute, private location: Location) { }

  private user: User;

  getUser(): void {
  const id = +this.route.snapshot.paramMap.get('id');
  this.userService.getUser(id).subscribe(user =>{
    this.user = user;
  });
  }

  updateUser(): void {
  this.userService.updateUser(this.user).subscribe(() => this.goBack());
  }

  goBack(): void {
  this.location.back();
  }
  ngOnInit() {
  this.getUser();
  }
  private setUserActive(){
    this.user.active=true;
  }
  private setUserInactive(){
    this.user.active=false;
  }

}
