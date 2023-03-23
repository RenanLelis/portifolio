import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
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
        next: (data) => {
          console.log(data);
          this.loading = false;
          this.router.navigate(['/']);
        },
        error: (e) => {
          console.log(e);
          this.loading = false;
          if (e.error && e.error.errorMessage) { this.errorMessage = getMessage(e.error.errorMessage); }
          else { this.errorMessage = getErrorMessage(); }
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
