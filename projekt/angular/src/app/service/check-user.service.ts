import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';

@Injectable()
export class CheckUserService {

  constructor(private http: HttpClient) { }
  
  
  getUserInformation(){
    return this.http.get('http://localhost:8080/projekt/api/home/check');
  }

}
