import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReactiveFormsModule } from '@angular/forms';
import { InterceptorService } from './service/interceptor.service';
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
import { LoadingComponent } from './view/components/loading/loading.component';
import { ErrorMessageComponent } from './view/components/error-message/error-message.component';
import { ConfirmMessageComponent } from './view/components/confirm-message/confirm-message.component';
import { SuccessMessageComponent } from './view/components/success-message/success-message.component';
import { FooterComponent } from './view/components/footer/footer.component';
import { HeaderComponent } from './view/components/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    TaskViewComponent,
    HomeComponent,
    LoginComponent,
    LogoutComponent,
    ForgetPasswordComponent,
    ResetPasswordComponent,
    NewUserComponent,
    UserActivationComponent,
    UserActivationRequestComponent,
    NotFoundComponent,
    LoadingComponent,
    ErrorMessageComponent,
    ConfirmMessageComponent,
    SuccessMessageComponent,
    FooterComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
