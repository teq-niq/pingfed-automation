import { Injectable } from '@angular/core';
import { User } from '../models/user';
import { HttpClient, HttpHeaders, HttpParams, HttpBackend, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UrlConstructService } from './url-construct.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  user?:User;

  constructor(private http: HttpClient,
    public urlsrvc: UrlConstructService) { }

  isLoggedOn():boolean{
    return this.user!=null && this.user.authenticateStatus;
  }

  loggedOnUser():User|undefined{
    return this.user;
  }

  testSuccessLogin()
  {
    this.user={username:'tester', authenticateStatus:true}
  }

  testFailedLogin()
  {
    this.user={username:'', authenticateStatus:false}
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
    this.user={username:'', authenticateStatus:false};
    let url = this.urlsrvc.mainUrl('logoutuser');
    
    
    this.http.get<string>(url, {withCredentials:true  
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
			
			this.user=data;
		}
    else{
      this.user={username:'', authenticateStatus:false};
    }
	}

  isLoggedOn$() :Observable<User>{
		let url = this.urlsrvc.mainUrl('getLoggedOnUser');
		//console.log("called session check from app component")
    return this.http.get<User>(url, {withCredentials:true});
	}

  login() {
    console.log('trying login');
    window.location.href = this.urlsrvc.mainUrl('tologin');
  
  }

  

}
