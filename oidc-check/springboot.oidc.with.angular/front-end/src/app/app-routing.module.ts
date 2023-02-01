import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UnProtectedComponent } from './unprotected/unprotected.component';
import { ProtectedComponent } from './protected/protected.component';
import { AuthGuard } from './guards/auth-guard.service';
import { AppComponent } from './app.component';


const routes: Routes = [
  { path: 'unprotected', component: UnProtectedComponent },
  { path: 'protected', component: ProtectedComponent, canActivate: [AuthGuard] }


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
