import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';
import { UserUpdateService } from 'src/app/service/user-update.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  formProfile: FormGroup = new FormGroup({
    "firstName": new FormControl('', Validators.required),
    "lastName": new FormControl(''),
  });

  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";

  constructor(private userUpdateService: UserUpdateService, private authService: AuthService) { }

  ngOnInit(): void {
    this.formProfile.patchValue({"lastName": this.authService.user.value!.lastName}); 
    this.formProfile.patchValue({"firstName": this.authService.user.value!.name});
  }

  updateProfile() {
    this.errorMessage = "";
    this.suscessMessage = "";
    if (this.formProfile.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userUpdateService.updateUserProfile(this.formProfile.get('firstName')!.value.trim(), this.formProfile.get('lastName')!.value.trim()).subscribe({
        next: (userData) => {
          this.loading = false;
          this.suscessMessage = getMessageOperationSucceded();
          this.authService.updateUserInfo(this.formProfile.get('firstName')!.value.trim(), this.formProfile.get('lastName')!.value.trim())
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
