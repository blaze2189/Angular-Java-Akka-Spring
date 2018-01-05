import { Injectable } from '@angular/core';

import {Session} from '../model/session';

@Injectable()
export class SessionService {

  private sessionData:Session;

  constructor() { }

  public setSessionData(sessionData:Session){
    this.sessionData=sessionData;
  }

  public getSessionData():Session{
    return this.sessionData
  }

}
