import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';

import { AppComponent } from './app.component';
import { DiscoveriesComponent } from './discoveries/discoveries.component';
import { DiscoveryService } from './discovery.service';
import { UserService } from './user.service';
import { UsersComponent } from './users/users.component';
import { AppRoutingModule } from './/app-routing.module';
import { EdituserComponent } from './edituser/edituser.component';
import { EditdiscoveryComponent } from './editdiscovery/editdiscovery.component';
import { DiscoveryComponent } from './discovery/discovery.component';
import { CommentService } from './comment.service';
import { CommentsComponent } from './comments/comments.component';
import { AdddiscoveryComponent } from './adddiscovery/adddiscovery.component';
import { AddComponent } from './add/add.component';
import { EventService } from './event.service';
import { EventsComponent } from './events/events.component';
import { AddeventComponent } from './addevent/addevent.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { BootstrapSwitchModule } from 'angular2-bootstrap-switch';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { EventpositionService } from './eventposition.service';
import { CheckUserService } from './check-user.service';
import { DiscoveryPageService } from './discoverypage.service';
import { PageService } from './page.service';
import { SearcheventbycityComponent } from './searcheventbycity/searcheventbycity.component';
import { ListofeventsComponent } from './listofevents/listofevents.component';
import { NameofcityService } from './nameofcity.service';
import { ReactiveFormsModule} from '@angular/forms';
import { SearchbypositionComponent } from './searchbyposition/searchbyposition.component';
import { DataService } from './data.service';

@NgModule({
  declarations: [
    AppComponent,
    DiscoveriesComponent,
    UsersComponent,
    EdituserComponent,
    EditdiscoveryComponent,
    DiscoveryComponent,
    CommentsComponent,
    AdddiscoveryComponent,
    AddComponent,
    EventsComponent,
    AddeventComponent,
    SearcheventbycityComponent,
    ListofeventsComponent,
    SearchbypositionComponent,
  ],
  imports: [
    ReactiveFormsModule,
    BrowserModule,
    FormsModule,
    BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot(),
    HttpClientModule,
    AppRoutingModule,
    LeafletModule.forRoot(),
    BootstrapSwitchModule.forRoot(),
    BrowserAnimationsModule
  ],
  providers: [DiscoveryService, UserService, CommentService, EventService, EventpositionService, CheckUserService, DiscoveryPageService, PageService, NameofcityService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
