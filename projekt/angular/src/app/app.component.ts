import { Component, OnInit, DoCheck, AfterViewInit  } from '@angular/core';
import { Router, NavigationEnd, NavigationStart } from '@angular/router';
import { DataService } from './data.service';
import { BaseUrl } from './baseurl.enum';
import { EndPoint } from './endpoint.enum';

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

  constructor(private router: Router) {
    router.events.subscribe((e) => {
      this.counter = 600;
    });
  }

  ngOnInit() {
    this.nav = document.getElementsByTagName("nav")[0];
    this.navHeight = this.nav.offsetHeight;
    this.initialCounter();
    this.addRouteChangeListener();
  }

  private addRouteChangeListener() {
    this.router.events.forEach((e) => {
      if (e instanceof NavigationStart) {
        this.panelBody = document.getElementById("main");
      }
    });
  }

  private initialCounter(): void {
    setInterval(() => {
      --this.counter;
      if (this.counter == 0) {
        window.location.href = BaseUrl.development + EndPoint.logout;

      }

    }, 1000);
  }

  ngDoCheck() {
    if (this.navHeight !== this.nav.offsetHeight) {
      this.navHeight = this.nav.offsetHeight;
    }
    if (this.panelBody === undefined || this.panelBody === null) {
      this.panelBody=document.getElementById("main");
    }
    if (this.panelBody !== undefined && this.panelBody !== null) {
      this.panelBody.style.marginTop = this.navHeight-20;
    }
  }

  private reloadCounter(){
  this.counter=600;
  }

}
