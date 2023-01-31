
import { Injectable } from '@angular/core';
// @ts-ignore
import base from './../../baseurl/base.json';

@Injectable({
  providedIn: 'root'
})


export class UrlConstructService {
  private url: string;
  private isOn4200: boolean;
  constructor() {

    this.isOn4200 = (window.location.port == '4200')
    this.url = base ? base['base'] : '';
  }

  getIsOn4200(): boolean {
    return this.isOn4200;
  }


  mainUrl(path: string) {
    let use = (this.getIsOn4200() === true) ? this.url  + path : path;
    return use;
  }
}
