import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './services/auth-guard.service';
import { LoginComponent } from './view/auth/login/login.component';
import { LogoutComponent } from './view/auth/logout/logout.component';
import { ForgetPasswordComponent } from './view/auth/forget-password/forget-password.component';
import { ResetPasswordComponent } from './view/auth/reset-password/reset-password.component';
import { NewUserComponent } from './view/auth/new-user/new-user.component';
import { UserActivationComponent } from './view/auth/user-activation/user-activation.component';
import { UserActivationRequestComponent } from './view/auth/user-activation-request/user-activation-request.component';
import { NotFoundComponent } from './view/not-found/not-found.component';
import { ProfileComponent } from './view/user/profile/profile.component';
import { ChangePasswordComponent } from './view/user/change-password/change-password.component';
import { DashboardComponent } from './view/tasks/dashboard/dashboard.component';
import { TasksViewComponent } from './view/tasks/tasks-view/tasks-view.component';
import { NewTaskComponent } from './view/tasks/new-task/new-task.component';
import { NewListComponent } from './view/tasks/new-list/new-list.component';

const routes: Routes = [
  {
    path: 'home', component: DashboardComponent, canActivate: [AuthGuardService],
    children: [
      { path: '', component: TasksViewComponent, canActivate: [AuthGuardService] },
      { path: 'list', component: NewListComponent, canActivate: [AuthGuardService] },
      { path: 'list/:id', component: NewListComponent, canActivate: [AuthGuardService] },
      { path: 'task', component: NewTaskComponent, canActivate: [AuthGuardService] },
      { path: 'task/:id', component: NewTaskComponent, canActivate: [AuthGuardService] },
      
      { path: 'mypassword', component: ChangePasswordComponent, canActivate: [AuthGuardService] },
      { path: 'profile', component: ProfileComponent, canActivate: [AuthGuardService] },
    ]
  },
  { path: '', redirectTo: '/home', pathMatch: 'full' },

  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent },
  { path: 'recoverpassword', component: ForgetPasswordComponent },
  { path: 'passwordreset/:email', component: ResetPasswordComponent },
  { path: 'userregistration', component: NewUserComponent },
  { path: 'useractivation/:email', component: UserActivationComponent },
  { path: 'useractivationrequest/:email', component: UserActivationRequestComponent },

  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
