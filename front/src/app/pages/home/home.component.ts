import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit() {
    this.router.navigate(['home',{outlets:{'home-router':['init']}}])
  }

/*  videos(){
    this.router.navigate(['home',{outlets:{'home-router':['videos']}}]);
  }

  home(){
    this.router.navigate(['home',{outlets:{'home-router':['init']}}]);
  }
*/
  changeContent(content:string){
    this.router.navigate(['home',{outlets:{'home-router':[content]}}]);
  }

}
