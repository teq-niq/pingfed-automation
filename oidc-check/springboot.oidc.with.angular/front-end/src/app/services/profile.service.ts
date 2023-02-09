import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { HttpClient, HttpHeaders, HttpParams, HttpBackend, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UrlConstructService } from './url-construct.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  user?:User|undefined;

  constructor(private http: HttpClient,
    public urlsrvc: UrlConstructService) { }

  isLoggedOn():boolean{
    return this.user!=null && this.user.authenticateStatus;
  }

  hasAuthority(authority:string):boolean
    {
        let ret:boolean=false;
        if(this.user!=null && this.user.authenticateStatus)
        {
          if(this.user.authorities!=null && this.user.authorities.length>0)
          {
              ret=this.user.authorities.indexOf(authority) > -1;
          }
        }
        
        return ret;
    }

 

  loggedOnUser():User|undefined{
    return this.user;
  }

  



  validateSession() {
		this.isLoggedOn$()
			.subscribe({
        next: (data) => {this.storeSession(data)},
        error: (e) => 	{this.sessionInvalidate();console.log('in error');console.log('Got Problem');console.log('after error');},
        complete: () => console.info('complete-validateSession') 
    });
	}

  sessionInvalidate() {
    this.user=undefined;
    let url = this.urlsrvc.mainUrl('logoutuser');
    
    
    this.http.get<string>(url, {withCredentials:true, headers:{"X-Requested-With": "XMLHttpRequest"}  
      }).subscribe({
      next: (v:string) => console.log(JSON.stringify(v)),
      error: (e) => console.error(e),
      complete: () => console.info('complete-logoutuser') 
  }
    );
	
	}

	storeSession(data:User) {
    console.log('data='+JSON.stringify(data));
		if (data !== null && data.username != null && data.authenticateStatus) {
			
			this.user=data;;
		}
    else{
      this.user=undefined;
      
    }
	}

  isLoggedOn$() :Observable<User>{
		let url = this.urlsrvc.mainUrl('getLoggedOnUser');
		//console.log("called session check from app component")
    return this.http.get<User>(url, {withCredentials:true, headers:{"X-Requested-With": "XMLHttpRequest"}});
	}

  login() {
    console.log('trying login');
    window.location.href = this.urlsrvc.mainUrl('tologin');
  
  }

  

}
