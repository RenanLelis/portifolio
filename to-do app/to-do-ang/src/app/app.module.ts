import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './view/components/header/header.component';
import { FooterComponent } from './view/components/footer/footer.component';
import { LoadingComponent } from './view/components/loading/loading.component';
import { SuccessMessageComponent } from './view/components/success-message/success-message.component';
import { ErrorMessageComponent } from './view/components/error-message/error-message.component';
import { ConfirmMessageComponent } from './view/components/confirm-message/confirm-message.component';
import { NotFoundComponent } from './view/not-found/not-found.component';
import { ChangePasswordComponent } from './view/user/change-password/change-password.component';
import { ProfileComponent } from './view/user/profile/profile.component';
import { LoginComponent } from './view/auth/login/login.component';
import { LogoutComponent } from './view/auth/logout/logout.component';
import { ForgetPasswordComponent } from './view/auth/forget-password/forget-password.component';
import { ResetPasswordComponent } from './view/auth/reset-password/reset-password.component';
import { NewUserComponent } from './view/auth/new-user/new-user.component';
import { UserActivationComponent } from './view/auth/user-activation/user-activation.component';
import { UserActivationRequestComponent } from './view/auth/user-activation-request/user-activation-request.component';
import { DashboardComponent } from './view/tasks/dashboard/dashboard.component';
import { ListsComponent } from './view/tasks/lists/lists.component';
import { NewListComponent } from './view/tasks/new-list/new-list.component';
import { NewTaskComponent } from './view/tasks/new-task/new-task.component';
import { TasksViewComponent } from './view/tasks/tasks-view/tasks-view.component';
import { ReactiveFormsModule } from '@angular/forms';
import { InterceptorService } from './services/interceptor.service';
import { TaskComponent } from './view/tasks/task/task.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    LoadingComponent,
    SuccessMessageComponent,
    ErrorMessageComponent,
    ConfirmMessageComponent,
    NotFoundComponent,
    ChangePasswordComponent,
    ProfileComponent,
    LoginComponent,
    LogoutComponent,
    ForgetPasswordComponent,
    ResetPasswordComponent,
    NewUserComponent,
    UserActivationComponent,
    UserActivationRequestComponent,
    DashboardComponent,
    ListsComponent,
    NewListComponent,
    NewTaskComponent,
    TasksViewComponent,
    TaskComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
