import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { UserService } from 'src/app/service/user.service';
import { passwordValidation } from 'src/app/validator/validator';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  email: string = this.route.snapshot.params['email'];
  loading: boolean = false;
  errorMessage: string = "";
  formPasswordReset: FormGroup = new FormGroup({
    "email": new FormControl(this.email, [Validators.required, Validators.email]),
    "newPasswordCode": new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]),
    "newPassword": new FormControl('', [Validators.required, passwordValidation]),
  });

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.formPasswordReset.value.email = this.email
  }

  resetPassword() {
    this.formPasswordReset.value.email = this.email
    if (!this.email || this.formPasswordReset.invalid) {
      this.errorMessage = getErrorMessageInputValues();
      return;
    }
    this.loading = true;
    this.userService.registerNerPasswordFromCode(this.email, this.formPasswordReset.value.newPassword, this.formPasswordReset.value.newPasswordCode).subscribe(
      response => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      err => {
        console.log(err);
        this.loading = false;
        this.errorMessage = getMessage(err.errorMessage)
      }
    )
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

}