import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';
import { UserUpdateService } from 'src/app/service/user-update.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  formMyPassword: FormGroup = new FormGroup({
    "password": new FormControl('', [Validators.required, Validators.minLength(5), this.authService.passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";
  firstName = "";
  lastName = "";

  constructor(private userUpdateService: UserUpdateService, private authService: AuthService) { }

  ngOnInit(): void {
    this.firstName = this.authService.user.value!.name; 
    this.lastName = this.authService.user.value!.lastName ? this.authService.user.value!.lastName! : "";
  }

  updateMyPassword() {
    this.errorMessage = "";
    this.suscessMessage = "";
    if (this.formMyPassword.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userUpdateService.updateUserPassword(this.formMyPassword.get('password')!.value.trim()).subscribe({
        next: (userData) => {
          this.loading = false;
          this.suscessMessage = getMessageOperationSucceded();
        },
        error: (e) => {
          console.log(e);
          this.loading = false;
          if (e.error && e.error.errorMessage) { 
            this.errorMessage = getMessage(e.error.errorMessage);
          } else { 
            this.errorMessage = getErrorMessage(); 
          }
        }
      });
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  closeMessage() {
    this.suscessMessage = "";
  }

}
