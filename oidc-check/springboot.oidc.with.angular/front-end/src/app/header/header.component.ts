import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileService } from '../services/profile.service';
import { User } from '../models/user';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  
login() {
  this.profileService.login();

}

logout() {
  this.profileService.sessionInvalidate();

}
  constructor(private profileService:ProfileService){}
  isLoggedOn():boolean{
    return this.profileService.isLoggedOn();
  }
  name():string{
    let name:string='';
    const user:User|undefined=this.profileService.loggedOnUser();
    if(user && user.authenticateStatus)
    {
      name=user.username;
    }
    return name;
  }
}
