import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formLogin: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, Validators.minLength(5), this.passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  login() {}

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
