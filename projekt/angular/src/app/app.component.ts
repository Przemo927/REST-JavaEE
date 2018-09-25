import {Component, DoCheck, OnInit} from "@angular/core";
import {NavigationEnd, NavigationStart, Router} from "@angular/router";
import {BaseUrl} from "./baseurl.enum";
import {EndPoint} from "./endpoint.enum";
import {ContentPdfService} from "./contentpdf.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, DoCheck {

  private counterSeconds = 600;
  private panelBody: any;
  private navHeight: number;
  private nav: any;
  private urlLogout: string;
  private mainMenu:any;

  constructor(private router: Router, private dataService:ContentPdfService) {
    router.events.subscribe(() => {
      this.counterSeconds = 600;
    });
  }

  ngOnInit() {
    this.urlLogout = window.location.protocol + BaseUrl.doubleUrlSeparator + BaseUrl.development + EndPoint.logout;
    this.nav = document.getElementsByTagName("nav")[0];
    this.navHeight = this.nav.offsetHeight;
    this.initialCounter();
    this.addRouteChangeListener();
  }

  private addRouteChangeListener() {
    this.router.events.forEach((event) => {
      if (event instanceof NavigationEnd) {
        this.panelBody = document.getElementById("main");
      }
    });
  }

  private initialCounter(): void {
    setInterval(() => {
      --this.counterSeconds;
      if (this.counterSeconds == 0) {
        window.location.href = this.urlLogout;
      }
      if (this.counterSeconds<0){
        this.counterSeconds=0;
      }

    }, 1000);
  }

  ngDoCheck() {
    this.setUpTopMarginOfMainDivDependOfPositionOfNavbar();
  }

  private setUpTopMarginOfMainDivDependOfPositionOfNavbar(){
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
    this.counterSeconds=600;
  }

  private sendContentToPdfModule(){
    if(this.panelBody!==null){
      this.dataService.addBehaviourSource(this.panelBody.outerHTML);
    }
  }
}
