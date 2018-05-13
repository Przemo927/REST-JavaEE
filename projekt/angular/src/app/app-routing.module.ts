import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersComponent } from './users/users.component';
import { EdituserComponent } from './edituser/edituser.component';
import { DiscoveriesComponent } from './discoveries/discoveries.component';
import { EditdiscoveryComponent } from './editdiscovery/editdiscovery.component';
import { DiscoveryComponent } from './discovery/discovery.component';
import { AdddiscoveryComponent } from './adddiscovery/adddiscovery.component';
import { EventsComponent } from './events/events.component';
import { AddeventComponent } from './addevent/addevent.component';
import { SearcheventbycityComponent } from './searcheventbycity/searcheventbycity.component';
import { ListofeventsComponent } from './listofevents/listofevents.component';
import { SearchbypositionComponent } from './searchbyposition/searchbyposition.component';
import {PagesComponent} from "./pages/pages.component";



const routes: Routes = [
  { path: 'users', component: UsersComponent },
  { path: '', component: DiscoveriesComponent,
    children:[
      {
        path: '',
        component: PagesComponent
      }
    ]
  },
  { path: 'editdiscovery/:id', component: EditdiscoveryComponent },
  { path: 'discovery/:id', component: DiscoveryComponent },
  { path: 'adddiscovery', component: AdddiscoveryComponent },
  { path: 'edituser/:id', component: EdituserComponent },
  { path: 'event', component: EventsComponent,
    children: [
      {
        path: 'searchByCity',
        component: SearcheventbycityComponent,
        children: [
          {
            path: '',
            component: ListofeventsComponent
          }
        ]
      },
      {
        path: 'listOfAllEvents',
        component: ListofeventsComponent
      },
      {
        path: 'searchByPosition',
        component: SearchbypositionComponent,
        children: [
          {
            path: '',
            component: ListofeventsComponent
          }
        ]
      },
      {
        path:'',redirectTo: 'searchByCity',pathMatch: 'full'
      }
    ]
  },
  { path: 'addevent', component: AddeventComponent },
  { path: '', redirectTo: '', pathMatch: 'full' },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
@NgModule({
   exports: [ RouterModule ],
   imports: [ RouterModule.forRoot(routes, {
useHash: true
}) ]

})
export class AppRoutingModule {

}
