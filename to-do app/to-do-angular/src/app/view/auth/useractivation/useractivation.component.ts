import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-useractivation',
  templateUrl: './useractivation.component.html',
  styleUrls: ['./useractivation.component.css']
})
export class UseractivationComponent implements OnInit {

  email: string = this.route.snapshot.params['email'];
  loading: boolean = false;
  errorMessage: string = "";
  formUserActivation: FormGroup = new FormGroup({
    "email": new FormControl(this.email, [Validators.required, Validators.email]),
    "activationCode": new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(6)]),
  });

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if (this.email === "" || this.authService.isUserLoggedIn()) {
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
    this.authService.activateUser(this.email, this.formUserActivation.value.activationCode).subscribe(
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
