import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-useractivation-request',
  templateUrl: './useractivation-request.component.html',
  styleUrls: ['./useractivation-request.component.css']
})
export class UseractivationRequestComponent implements OnInit {

  email: string = this.route.snapshot.params['email'];
  loading: boolean = false;
  errorMessage: string = "";
  formUserActivationRequest: FormGroup = new FormGroup({
    "email": new FormControl(this.email, [Validators.required, Validators.email]),
  });

  constructor(private authService: AuthService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    if (this.email === "" || this.authService.isUserLoggedIn()) {
      this.router.navigate(['/']);
    } else {
      this.formUserActivationRequest.value.email = this.email
    }
  }

  requestUserActivation() {
    this.formUserActivationRequest.value.email = this.email
    if (!this.email || this.formUserActivationRequest.invalid) {
      this.errorMessage = getErrorMessageInputValues();
      return;
    }
    this.loading = true;
    this.authService.requestUserActivation(this.email).subscribe(
      response => {
        console.log(response);
        this.loading = false;
        this.router.navigate(['/useractivation/' + this.email]);
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
