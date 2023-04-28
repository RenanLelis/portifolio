import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { UserService } from 'src/app/service/user.service';
import { passwordValidation } from 'src/app/validator/validator';

@Component({
  selector: 'app-new-user',
  templateUrl: './new-user.component.html',
  styleUrls: ['./new-user.component.css']
})
export class NewUserComponent {

  formUserRegistration: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, Validators.minLength(5), passwordValidation]),
    "name": new FormControl('', Validators.required),
    "lastName": new FormControl(null),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private userService: UserService, private router: Router) { }

  registerUser() {
    this.errorMessage = "";
    if (this.formUserRegistration.get('email')!.invalid || this.formUserRegistration.get('password')!.invalid || this.formUserRegistration.get('name')!.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      console.log(this.formUserRegistration);
      this.userService.registerUser(
          this.formUserRegistration.get('email')!.value.trim(), 
          this.formUserRegistration.get('password')!.value.trim(),
          this.formUserRegistration.get('name')!.value.trim(),
          this.formUserRegistration.get('lastName') != null ? this.formUserRegistration.get('lastName')!.value.trim() : ''
          )
          .subscribe({
            next: (data) => {
              this.loading = false;
              this.router.navigate(['/useractivation/' + this.formUserRegistration.get('email')!.value.trim()]);
            },
            error: (e) => {
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