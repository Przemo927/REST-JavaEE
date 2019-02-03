import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

import {BsDropdownModule} from "ngx-bootstrap/dropdown";
import {TooltipModule} from "ngx-bootstrap/tooltip";
import {ModalModule} from "ngx-bootstrap/modal";

import {AppComponent} from "./app.component";
import {DiscoveriesComponent} from "./discoveries/discoveries.component";
import {DiscoveryService} from "./service/discovery.service";
import {UserService} from "./service/user.service";
import {UsersComponent} from "./users/users.component";
import {AppRoutingModule} from "./app-routing.module";
import {EdituserComponent} from "./edituser/edituser.component";
import {EditdiscoveryComponent} from "./editdiscovery/editdiscovery.component";
import {DiscoveryComponent} from "./discovery/discovery.component";
import {CommentService} from "./service/comment.service";
import {CommentsComponent} from "./comments/comments.component";
import {AdddiscoveryComponent} from "./adddiscovery/adddiscovery.component";
import {AddComponent} from "./add/add.component";
import {EventService} from "./service/event.service";
import {EventsComponent} from "./events/events.component";
import {AddeventComponent} from "./addevent/addevent.component";
import {LeafletModule} from "@asymmetrik/ngx-leaflet";
import {BootstrapSwitchModule} from "angular2-bootstrap-switch";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {EventpositionService} from "./service/eventposition.service";
import {CheckUserService} from "./service/check-user.service";
import {PageService} from "./service/page.service";
import {SearcheventbycityComponent} from "./searcheventbycity/searcheventbycity.component";
import {ListofeventsComponent} from "./listofevents/listofevents.component";
import {NameofcityService} from "./service/nameofcity.service";
import {SearchbypositionComponent} from "./searchbyposition/searchbyposition.component";
import {DataService} from "./service/data.service";
import {PagesComponent} from "./pages/pages.component";
import {ValidatabletextareaComponent} from "./validatabletextarea/validatabletextarea.component";
import {EncryptedService} from "./service/encrypted.service";
import {DiscoveriesEncryptedComponent} from "./discoveries/discoveriesencrypted.component";
import {EncryptedComponent} from "./encrypted/encrypted.component";
import {KeyService} from "./service/key.service";
import {ValidatableinputComponent} from "./validatableinput/validatableinput.component";
import {CalendarModule} from "primeng/calendar";
import {LeftmenuComponent} from "./leftmenu/leftmenu.component";
import {ProfileComponent} from "./profile/profile.component";
import {HeaderComponent} from "./header/header.component";
import {PdfmoduleComponent} from "./pdfmodule/pdfmodule.component";
import {ContentPdfService} from "./service/contentpdf.service";

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
    PagesComponent,
    ValidatabletextareaComponent,
    DiscoveriesEncryptedComponent,
    EncryptedComponent,
    ValidatableinputComponent,
    LeftmenuComponent,
    ProfileComponent,
    HeaderComponent,
    PdfmoduleComponent,
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
    BrowserAnimationsModule,
    CalendarModule
  ],
  providers: [DiscoveryService, UserService, CommentService, EventService, EventpositionService, CheckUserService, PageService, NameofcityService, DataService, EncryptedService, KeyService, ContentPdfService],
  bootstrap: [AppComponent]
})
export class AppModule { }
