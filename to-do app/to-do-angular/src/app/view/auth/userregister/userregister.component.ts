import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-userregister',
  templateUrl: './userregister.component.html',
  styleUrls: ['./userregister.component.css']
})
export class UserregisterComponent implements OnInit {

  formUserRegistration: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, Validators.minLength(5), this.authService.passwordValidation]),
    "name": new FormControl('', Validators.required),
    "lastName": new FormControl(null),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    }
  }

  registerUser() {
    this.errorMessage = "";
    if (this.formUserRegistration.get('email')!.invalid || this.formUserRegistration.get('password')!.invalid || this.formUserRegistration.get('name')!.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      console.log(this.formUserRegistration);
      this.authService.registerUser(
          this.formUserRegistration.get('email')!.value.trim(), 
          this.formUserRegistration.get('password')!.value.trim(),
          this.formUserRegistration.get('name')!.value.trim(),
          this.formUserRegistration.get('lastName') != null ? this.formUserRegistration.get('lastName')!.value.trim() : ''
          )
          .subscribe({
            next: (data) => {
              console.log(data);
              this.loading = false;
              this.router.navigate(['/useractivation/' + this.formUserRegistration.get('email')!.value.trim()]);
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

}
