import { Injectable } from '@angular/core';
import {Http,Response,RequestOptions, Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class HttpLoginService {

  private url:string="http://localhost:8080/login";
  //private url:string="http://localhost:8000/springUser";
  private fixUrl='http://api.fixer.io/latest';
  constructor(public http:Http ) { }

  get(user:string,password:string):Observable<Response>{
    var url = this.url+"/"+user+"/"+password;
    return this.http.get(url).map(res=>res);
    //return this.http.get(this.fixUrl).map(res=>res);
  }

}
