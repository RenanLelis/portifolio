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

const routes: Routes = [
  { path: '', component: HomeComponent, canActivate: [AuthGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'recoverpassword', component: ForgetpasswordComponent },
  { path: 'passwordreset', component: NewpasswordComponent },
  { path: 'userregistration', component: UserregisterComponent },
  { path: 'useractivation', component: UseractivationComponent },

  { path: 'not-found', component: NotFoundComponent },
  { path: '**', redirectTo: '/not-found' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
