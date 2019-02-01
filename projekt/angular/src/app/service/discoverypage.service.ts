import {Injectable} from "@angular/core";
import {Discovery} from "../model/discovery";
import {HttpClient} from "@angular/common/http";
import {PageService} from "./page.service";

@Injectable()
export class DiscoveryPageService extends PageService<Discovery> {

    constructor(public http: HttpClient) {
    super(http, 'http://localhost:8080/projekt/api/discovery');
  }


}
