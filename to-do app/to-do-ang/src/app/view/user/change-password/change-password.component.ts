import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from 'src/app/message/message';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  formMyPassword: FormGroup = new FormGroup({
    "password": new FormControl('', [Validators.required, Validators.minLength(5), this.userService.passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";
  firstName = "";
  lastName = "";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.firstName = this.userService.user.value!.name;
    this.lastName = this.userService.user.value!.lastName ? this.userService.user.value!.lastName! : "";
  }

  updateMyPassword() {
    if (this.formMyPassword.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userService.updateUserPassword(this.formMyPassword.get('password')!.value.trim()).subscribe({
        next: (userData) => {
          this.loading = false;
          this.suscessMessage = getMessageOperationSucceded();
        },
        error: (e) => { this.handleError(e); }
      });
    }
  }

  handleError(e: any) {
    console.log(e);
    this.loading = false;
    if (e.error && e.error.errorMessage) {
      this.errorMessage = getMessage(e.error.errorMessage);
    } else {
      this.errorMessage = getErrorMessage();
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  closeMessage() {
    this.suscessMessage = "";
    this.router.navigate(['/home/']);
  }

}
