import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { UrlConstructService } from '../services/url-construct.service';

@Component({
  selector: 'app-comp1',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './unprotected..component.html',
  styleUrls: ['./unprotected..component.css']
})
export class UnProtectedComponent {

  regularResponse?:string;
  protectedResponse?:string;
  constructor(private http: HttpClient,
    public urlsrvc: UrlConstructService)
  {

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

}
