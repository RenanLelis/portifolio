import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { ForgetpasswordComponent } from './view/auth/forgetpassword/forgetpassword.component';
import { LoginComponent } from './view/auth/login/login.component';
import { NewpasswordComponent } from './view/auth/newpassword/newpassword.component';
import { UseractivationComponent } from './view/auth/useractivation/useractivation.component';
import { UserregisterComponent } from './view/auth/userregister/userregister.component';
import { NotFoundComponent } from './view/not-found/not-found.component';
import { HomeComponent } from './view/tasks/home/home.component';
import { LogoutComponent } from './view/auth/logout/logout.component';
import { ChangePasswordComponent } from './view/user/change-password/change-password.component';
import { ProfileComponent } from './view/user/profile/profile.component';
import { UseractivationRequestComponent } from './view/auth/useractivation-request/useractivation-request.component';
import { ListCreationComponent } from './view/tasks/list-creation/list-creation.component';

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuardService] },
  { path: 'listedit', component: ListCreationComponent, canActivate: [AuthGuardService] },
  { path: 'listedit/:id', component: ListCreationComponent, canActivate: [AuthGuardService] },

  { path: 'mypassword', component: ChangePasswordComponent, canActivate: [AuthGuardService] },
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService] },
  
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'recoverpassword', component: ForgetpasswordComponent },
  { path: 'passwordreset/:email', component: NewpasswordComponent },
  { path: 'userregistration', component: UserregisterComponent },
  { path: 'useractivation/:email', component: UseractivationComponent },
  { path: 'useractivationrequest/:email', component: UseractivationRequestComponent },

  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
