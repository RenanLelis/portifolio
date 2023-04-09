import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { InterceptorService } from './service/interceptor.service';
import { LoginComponent } from './view/auth/login/login.component';
import { ForgetpasswordComponent } from './view/auth/forgetpassword/forgetpassword.component';
import { NewpasswordComponent } from './view/auth/newpassword/newpassword.component';
import { UserregisterComponent } from './view/auth/userregister/userregister.component';
import { UseractivationComponent } from './view/auth/useractivation/useractivation.component';
import { HeaderComponent } from './view/components/header/header.component';
import { FooterComponent } from './view/components/footer/footer.component';
import { HomeComponent } from './view/tasks/home/home.component';
import { NotFoundComponent } from './view/not-found/not-found.component';
import { LoadingComponent } from './view/components/loading/loading.component';
import { ErrorMessageComponent } from './view/components/error-message/error-message.component';
import { LogoutComponent } from './view/auth/logout/logout.component';
import { ChangePasswordComponent } from './view/user/change-password/change-password.component';
import { ProfileComponent } from './view/user/profile/profile.component';
import { UseractivationRequestComponent } from './view/auth/useractivation-request/useractivation-request.component';
import { SuccessMessageComponent } from './view/components/success-message/success-message.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ForgetpasswordComponent,
    NewpasswordComponent,
    UserregisterComponent,
    UseractivationComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    NotFoundComponent,
    LoadingComponent,
    ErrorMessageComponent,
    LogoutComponent,
    ChangePasswordComponent,
    ProfileComponent,
    UseractivationRequestComponent,
    SuccessMessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: InterceptorService, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
