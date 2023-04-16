import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent implements OnInit {

  formForgetPassword: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
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

  sendRequest() {
    if (this.formForgetPassword.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userService.forgotPassword(this.formForgetPassword.get('email')!.value.trim()).subscribe({
        next: (data) => {
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
