import { Component, OnInit, DoCheck, AfterViewInit  } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { DataService } from './data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, DoCheck {

  private counter = 600;
  private panelBody: any;
  private navHeight: number;
  private nav: any;

  ngOnInit() {
    this.nav = document.getElementsByTagName("nav")[0];
    this.navHeight = this.nav.offsetHeight;
  setInterval(() => {
    --this.counter;
    if(this.counter==0){
        window.location.href='http://localhost:8080/projekt/api/logout';

    }

  }, 1000);

  }
  changeWidth() {
    console.log(this.nav.offsetHeight);
  }

  ngDoCheck() {
    if (this.navHeight !== this.nav.offsetHeight) {
      this.navHeight = this.nav.offsetHeight;
    }
    let panelBody = document.getElementById("main");
    console.log(panelBody);
    if (this.panelBody === undefined && panelBody !== undefined && panelBody !== null) {
      this.panelBody=document.getElementById("main");
    }
    this.navHeight = document.getElementsByTagName("nav")[0].offsetHeight;
    if (this.panelBody !== undefined) {
      this.panelBody.style.marginTop = this.navHeight-20;
    }
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
