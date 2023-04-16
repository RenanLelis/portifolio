import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage, getMessageOperationSucceded } from 'src/app/message/message';
import { UserService } from 'src/app/services/user.service';

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

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.formProfile.patchValue({"lastName": this.userService.user.value!.lastName}); 
    this.formProfile.patchValue({"firstName": this.userService.user.value!.name});
  }

  updateProfile() {
    if (this.formProfile.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userService.updateUserProfile(this.formProfile.get('firstName')!.value.trim(), this.formProfile.get('lastName')!.value.trim()).subscribe({
        next: (userData) => {
          this.loading = false;
          this.suscessMessage = getMessageOperationSucceded();
          this.userService.updateUserInfo(this.formProfile.get('firstName')!.value.trim(), this.formProfile.get('lastName')!.value.trim())
        },
        error: (e) => {
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
    this.router.navigate(['/home/']);
  }

}