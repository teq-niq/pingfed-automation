import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, Route } from '@angular/router';
import { Observable } from 'rxjs';
import { ProfileService } from '../services/profile.service';
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private profileService: ProfileService, private router: Router) { }
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.profileService.isLoggedOn()) {
      return true;
  }
  
  // navigate to home page
  this.router.navigate(['/']);
  // you can save redirect url so after authing we can move them back to the page they requested
  return false;
  }
}
