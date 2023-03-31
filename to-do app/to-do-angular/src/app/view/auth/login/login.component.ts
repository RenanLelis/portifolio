import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { STATUS_ACTIVE, UserData } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formLogin: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, Validators.minLength(5), this.authService.passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  login() {
    this.errorMessage = "";
    if (this.formLogin.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.authService.login(this.formLogin.get('email')!.value.trim(), this.formLogin.get('password')!.value.trim()).subscribe({
        next: (userData) => {
          this.loading = false;
          if ((userData as UserData).status === STATUS_ACTIVE) {
            this.router.navigate(['/']);
          } else {
            this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]);
          }
        },
        error: (e) => {
          console.log(e);
          this.loading = false;
          if (e.error && e.error.errorMessage) { 
            if (e.error.errorMessage === "MSE07") {this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]);}
            else { this.errorMessage = getMessage(e.error.errorMessage); }
          } else { 
            this.errorMessage = getErrorMessage(); 
          }
        }
      });
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  passwordValidation(control: FormControl) {
    if (control.value === null || control.value === '' || !/\d/.test(control.value)) {
      return { "invalidPassword": true };
    }
    return null;
  }

  


}
