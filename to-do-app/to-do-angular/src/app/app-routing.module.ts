import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './service/auth-guard.service';
import { TaskViewComponent } from './view/task/task-view/task-view.component';
import { HomeComponent } from './view/task/home/home.component';
import { LoginComponent } from './view/auth/login/login.component';
import { LogoutComponent } from './view/auth/logout/logout.component';
import { ForgetPasswordComponent } from './view/auth/forget-password/forget-password.component';
import { ResetPasswordComponent } from './view/auth/reset-password/reset-password.component';
import { NewUserComponent } from './view/auth/new-user/new-user.component';
import { UserActivationComponent } from './view/auth/user-activation/user-activation.component';
import { UserActivationRequestComponent } from './view/auth/user-activation-request/user-activation-request.component';
import { NotFoundComponent } from './view/not-found/not-found.component';
import { LogoutGuardService } from './service/logout-guard.service';

const routes: Routes = [
  {
    path: 'home', component: HomeComponent, canActivate: [AuthGuardService],
    children: [
      { path: '', component: TaskViewComponent, canActivate: [AuthGuardService] },
    ]
  },

  { path: 'login', component: LoginComponent, canActivate: [LogoutGuardService] },
  { path: 'logout', component: LogoutComponent },
  { path: 'recoverpassword', component: ForgetPasswordComponent, canActivate: [LogoutGuardService] },
  { path: 'passwordreset/:email', component: ResetPasswordComponent, canActivate: [LogoutGuardService] },
  { path: 'userregistration', component: NewUserComponent, canActivate: [LogoutGuardService] },
  { path: 'useractivation/:email', component: UserActivationComponent, canActivate: [LogoutGuardService] },
  { path: 'useractivationrequest/:email', component: UserActivationRequestComponent, canActivate: [LogoutGuardService] },

  { path: '', redirectTo: '/home', pathMatch: 'full' },
  
  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
