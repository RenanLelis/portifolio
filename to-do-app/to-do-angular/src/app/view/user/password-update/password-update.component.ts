import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from 'src/app/msg/messages';
import { UserService } from 'src/app/service/user.service';
import { passwordValidation } from 'src/app/validator/validator';

@Component({
  selector: 'app-password-update',
  templateUrl: './password-update.component.html',
  styleUrls: ['./password-update.component.css']
})
export class PasswordUpdateComponent implements OnInit {

  formMyPassword: FormGroup = new FormGroup({
    "password": new FormControl('', [Validators.required, Validators.minLength(5), passwordValidation]),
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