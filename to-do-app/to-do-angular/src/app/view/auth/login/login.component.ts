import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { STATUS_ACTIVE, UserData } from 'src/app/model/user';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { UserService } from 'src/app/service/user.service';
import { passwordValidation } from 'src/app/validator/validator';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formLogin: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
      if (this.userService.isUserLoggedIn()) {
        this.router.navigate(['/']);
      }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  login() {
    if (this.formLogin.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userService.login(this.formLogin.get('email')!.value.trim(), this.formLogin.get('password')!.value.trim()).subscribe({
        next: (userData) => { 
          this.loading = false; 
          this.userLoginHandler(userData as UserData) 
        },
        error: (err) => {
          this.loading = false;
          this.handleError(err);
        }
      });
    }
  }

  userLoginHandler(userData: UserData) {
    this.loading = false;
    if ((userData).status === STATUS_ACTIVE) {
      this.router.navigate(['/']);
    } else {
      this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]);
    }
  }

  handleError(e: any) {
    console.log(e);
    if (e.error && e.error.errorMessage) {
      if (e.error.errorMessage === "MSE07") { this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]); }
      else { this.errorMessage = getMessage(e.error.errorMessage); }
    } else {
      this.errorMessage = getErrorMessage();
    }
  }


}
