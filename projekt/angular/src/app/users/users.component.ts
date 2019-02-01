import { Component, OnInit } from '@angular/core';
import { User } from '../model/user';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent implements OnInit {

  constructor(private userService: UserService) { }

  users: User[];
  getUsers(): void {
  this.userService.getUsers().subscribe(users => {
    users.map((user)=>{
      user.lastLogin=new Date(user.lastLogin);
    });
    this.users = users;
  });
  }

  ngOnInit() {
  this.getUsers();
  }

}
