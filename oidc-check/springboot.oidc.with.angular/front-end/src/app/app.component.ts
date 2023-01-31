import { CommonModule } from '@angular/common';
import { Component, importProvidersFrom, OnInit } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UnProtectedComponent } from './unprotected/unprotected.component';
import { ProtectedComponent } from './protected/protected.component';
import { AuthGuard } from './guards/auth-guard.service';
import { ProfileService } from './services/profile.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],


})
export class AppComponent implements OnInit{

  constructor(private profileService: ProfileService) {

  }


  isLoggedOn():boolean{
    return this.profileService.isLoggedOn();
  }

  ngOnInit() {
    console.log('validating');
    this.profileService.validateSession();
  }
  title = 'front-end';

  }
