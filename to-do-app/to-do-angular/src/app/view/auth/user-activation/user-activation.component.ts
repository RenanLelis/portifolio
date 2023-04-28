import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-user-activation',
  templateUrl: './user-activation.component.html',
  styleUrls: ['./user-activation.component.css']
})
export class UserActivationComponent implements OnInit {

  email: string = this.route.snapshot.params['email'];
  loading: boolean = false;
  errorMessage: string = "";
  formUserActivation: FormGroup = new FormGroup({
    "email": new FormControl(this.email, [Validators.required, Validators.email]),
    "activationCode": new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]),
  });

  constructor(private userService: UserService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if (this.email === "" || this.userService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    } else {
      this.formUserActivation.value.email = this.email
    }
  }

  activateUser() {
    this.formUserActivation.value.email = this.email
    if (!this.email || this.formUserActivation.invalid) {
      this.errorMessage = getErrorMessageInputValues();
      return;
    }
    this.loading = true;
    this.userService.activateUser(this.email, this.formUserActivation.value.activationCode).subscribe(
      response => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      err => {
        this.loading = false;
        this.errorMessage = getMessage(err.errorMessage)
      }
    )
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

}