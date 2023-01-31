import { Component } from '@angular/core';
import { CommonModule, JsonPipe } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { UrlConstructService } from '../services/url-construct.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-comp2',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './protected.component.html',
  styleUrls: ['./protected.component.css']
})
export class ProtectedComponent {
  protectedResponse?:string;
  constructor(private http: HttpClient,
    public urlsrvc: UrlConstructService)
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

}
