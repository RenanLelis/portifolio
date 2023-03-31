import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-forgetpassword',
  templateUrl: './forgetpassword.component.html',
  styleUrls: ['./forgetpassword.component.css']
})
export class ForgetpasswordComponent implements OnInit {

  formForgetPassword: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  sendRequest() {
    this.errorMessage = "";
    if (this.formForgetPassword.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.authService.forgotPassword(this.formForgetPassword.get('email')!.value.trim()).subscribe({
        next: (data) => {
          console.log(data);
          this.loading = false;
          this.router.navigate(['/passwordreset/' + this.formForgetPassword.get('email')!.value.trim()]);
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



}
