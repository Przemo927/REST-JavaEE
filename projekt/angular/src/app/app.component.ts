import { Component, OnInit  } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

private counter=600;

ngOnInit() {
  
  setInterval(() => {
    --this.counter;
    if(this.counter==0){
        window.location.href='http://localhost:8080/projekt/api/logout';
        
    }
    
  }, 1000);
  
  
}

constructor(private router: Router) {
    router.events.subscribe((e) => {
        this.counter=600;
    });
  }
  reloadCounter(){
  this.counter=600;
  }
  
}
