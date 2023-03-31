import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-newpassword',
  templateUrl: './newpassword.component.html',
  styleUrls: ['./newpassword.component.css']
})
export class NewpasswordComponent implements OnInit {

  email: string = this.route.snapshot.params['email'];
  loading: boolean = false;
  errorMessage: string = "";
  formPasswordReset: FormGroup = new FormGroup({
    "email": new FormControl(this.email, [Validators.required, Validators.email]),
    "newPasswordCode": new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]),
    "newPassword": new FormControl('', [Validators.required, this.authService.passwordValidation]),
  });

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if (this.email === "" || this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    } else {
      this.formPasswordReset.value.email = this.email
    }
  }

  resetPassword() {
    this.formPasswordReset.value.email = this.email
    if (!this.email || this.formPasswordReset.invalid) {
      this.errorMessage = getErrorMessageInputValues();
      return;
    }
    this.loading = true;
    this.authService.registerNerPasswordFromCode(this.email, this.formPasswordReset.value.newPassword, this.formPasswordReset.value.newPasswordCode).subscribe(
      response => {
        console.log(response);
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
