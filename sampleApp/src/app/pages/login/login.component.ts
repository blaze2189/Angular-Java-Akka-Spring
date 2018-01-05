import { Component, OnInit } from '@angular/core';
import {HttpLoginService} from '../../service/http-login.service';
import { CookieService } from 'angular2-cookie/services/cookies.service';
import {Session} from '../../model/session';
import {SessionService} from '../../service/session-service.service';
//import {AuthHttp} from '../../module/auth/auth.module';
import { AuthHttp } from 'angular2-jwt-session';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  private user:string;
  private password:string;
  private session:Session;
  private someThing:string;
  private valid:boolean=true;
  private errorMessage:string;

  constructor(private router:Router,
    private httpLoginService:HttpLoginService,
    private cookie:CookieService,
    private sessionData:SessionService ) { }

  ngOnInit() {
    this.user="jlopez@tiempodevelopment.com"
  }

   login(){
     console.log("ready to login "+this.user+":"+this.password);
     this.httpLoginService.get(this.user,this.password).subscribe(res=>this.printResult(res),err=>{console.log("error "+err)});
   }

   printSession(response){
     console.log(response.json());
     this.session=response.json();
     console.log(this.session);
     this.sessionData.setSessionData(this.session) ;
     console.log(this.session.token);
     console.log(this.session.token!=null);
     this.cookie.put("sessionToken",this.session.token);
     this.router.navigate(['home']);
   }

   private printResult(result){
     console.log(result.json());
     console.log("value of session");
    var session:Session =result.json();
    if(session.code===200){
      console.log("valid session");
      var user={
        "user":this.user,
        "password":this.password
      }
      this.cookie.putObject("user",user);
      console.log(this.cookie.getObject("user"));
    }else{
      console.log("result code "+session.code+":"+session.message);

      this.valid=false;
      this.errorMessage=session.message;
    }
    console.log(session.token);
    this.router.navigate(['home']);
   }

   ask(){
     this.cookie.removeAll();
   }

}
