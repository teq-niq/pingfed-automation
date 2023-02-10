import { Component } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { UrlConstructService } from '../services/url-construct.service';
import { HttpClient } from '@angular/common/http';
import { ProfileService } from '../services/profile.service';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-comp2',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './unprotected.component.html',
  styleUrls: ['./unprotected.component.css']
})
export class UnProtectedComponent {
  regularResponse?:string;
  protectedResponse?:string;
  fooResponse?:string;
  barResponse?:string;
  profileResponse?:string;
  showInnacessible: boolean=false;


  hasAuthority(authority:string):boolean{
    return this.profileService.hasAuthority(authority)||this.showInnacessible;
  }
  isLoggedOn():boolean{
    return this.profileService.isLoggedOn()||this.showInnacessible;
  }

  constructor(private http: HttpClient,
    public urlsrvc: UrlConstructService, 
    private profileService:ProfileService)
  {

  }
  protected()
  {
    let url = this.urlsrvc.mainUrl('secured');
		//console.log("called session check from app component")
    this.http.get<any>(url, {withCredentials:true}).subscribe({
      next: (data) => this.protectedResponse=JSON.stringify(data),
      error: (e) => 	this.protectedResponse='Got Problem',
      complete: () => console.info('complete') 
  });
  }

  regular()
  {
    let url = this.urlsrvc.mainUrl('unsecured');
		//console.log("called session check from app component")
    this.http.get<any>(url, {withCredentials:true}).subscribe({
      next: (data) => this.regularResponse=JSON.stringify(data),
      error: (e) => 	this.regularResponse='Got Problem',
      complete: () => console.info('complete') 
  });
  }

  foo()
  {
    let url = this.urlsrvc.mainUrl('foo');
		//console.log("called session check from app component")
    this.http.get<any>(url, {withCredentials:true, headers:{"X-Requested-With": "XMLHttpRequest"}}).subscribe({
      next: (data) => this.fooResponse=JSON.stringify(data),
      error: (e) => 	{this.fooResponse='status:'+e.status+'Got Problem'},
      complete: () => console.info('complete') 
  });
  }

  bar()
  {
    let url = this.urlsrvc.mainUrl('bar');
		//console.log("called session check from app component")
    this.http.get<any>(url, {withCredentials:true, headers:{"X-Requested-With": "XMLHttpRequest"}}).subscribe({
      next: (data) => this.barResponse=JSON.stringify(data),
      error: (e) => 	{this.barResponse='status:'+e.status+'Got Problem'},
      complete: () => console.info('complete') 
  });
  }

  profile()
  {
    let url = this.urlsrvc.mainUrl('profile');
		//console.log("called session check from app component")
    this.http.get<any>(url, {withCredentials:true, headers:{"X-Requested-With": "XMLHttpRequest"}}).subscribe({
      next: (data) => this.profileResponse=JSON.stringify(data),
      error: (e) => 	{this.profileResponse='status:'+e.status+'Got Problem'},
      complete: () => console.info('complete') 
  });
  }

}
