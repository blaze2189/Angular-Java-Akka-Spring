import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { routing } from './app.routes';

import { AppComponent } from './app.component';
import { LoginComponent } from './pages/login/login.component';

import {HttpModule} from '@angular/http';
import {HttpLoginService} from   './service/http-login.service';
import {ActiveTokenService} from './service/active-token.service';

import { CookieService } from 'angular2-cookie/services/cookies.service';

import { HomeComponent } from './pages/home/home.component';
import { InitPageComponent } from './pages/init-page/init-page.component';
import { PersonalComponent } from './pages/personal/personal.component';
import { VideosComponent } from './pages/videos/videos.component';
import { ActorsComponent } from './pages/actors/actors.component';
import { MoviesComponent } from './pages/movies/movies.component';
import { SessionService } from './service/session-service.service';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    InitPageComponent,
    PersonalComponent,
    VideosComponent,
    ActorsComponent,
    MoviesComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    routing,HttpModule
  ],
  providers: [HttpLoginService,
    CookieService,
    ActiveTokenService,
    SessionService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
