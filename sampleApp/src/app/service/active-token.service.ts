import { Injectable } from '@angular/core';
import {CanActivate} from '@angular/router';
import { CookieService } from 'angular2-cookie/services/cookies.service';

import { SessionService } from './session-service.service'
import {Router} from '@angular/router';

@Injectable()
export class ActiveTokenService implements CanActivate {

  constructor(private router:Router,
    private cookieService:CookieService,
    private sessionData:SessionService) { }

  canActivate(){
    var active:boolean=false;
    /*if(this.sessionData!=undefined && this.sessionData!=null){
      var tokenSession =this.sessionData.getSessionData().token;
      console.log("in activetokenservice "+tokenSession);
      console.log("session data: "+this.sessionData.getSessionData().token);
      active=tokenSession!=null && tokenSession!="";
    }*/
    var tokenSession =this.cookieService.get("sessionToken");
    console.log(tokenSession);
    active = tokenSession!=null && tokenSession!="";
    if(!active){
      console.log("not an active session");
    this.router.navigate(['/']);
    }
    return active;
  }

}
