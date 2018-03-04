import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UsersComponent } from './users/users.component';
import { EdituserComponent } from './edituser/edituser.component';
import { DiscoveriesComponent } from './discoveries/discoveries.component';
import { EditdiscoveryComponent } from './editdiscovery/editdiscovery.component';
import { DiscoveryComponent } from './discovery/discovery.component';
import { AdddiscoveryComponent } from './adddiscovery/adddiscovery.component';
import { EventsComponent } from './events/events.component';
import { AddeventComponent } from './addevent/addevent.component';



const routes: Routes = [
  { path: 'users', component: UsersComponent },
  { path: '', component: DiscoveriesComponent },
  { path: 'editdiscovery/:id', component: EditdiscoveryComponent },
  { path: 'discovery/:id', component: DiscoveryComponent },
  { path: 'adddiscovery', component: AdddiscoveryComponent },
  { path: 'edituser/:id', component: EdituserComponent },
  { path: 'events', component: EventsComponent },
  { path: 'addevent', component: AddeventComponent },
  { path: '', redirectTo: '', pathMatch: 'full' },
];
@NgModule({
  exports: [ RouterModule ],
  imports: [ RouterModule.forRoot(routes, {
    useHash: true
  }) ]

})
export class AppRoutingModule {

}
