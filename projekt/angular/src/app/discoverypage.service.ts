import { Injectable } from '@angular/core';
import { DiscoveryService } from './discovery.service';
import { Page } from './page';
import { Discovery } from './discovery';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {Observable} from 'rxjs/Rx';
import { PageService } from './page.service';

@Injectable()
export class DiscoveryPageService extends PageService<Discovery> {

    constructor(public http: HttpClient) {
    super(http, 'http://localhost:8080/projekt/api/discovery');
  }


}
