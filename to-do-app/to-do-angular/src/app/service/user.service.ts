import { Injectable } from '@angular/core';
import { BASE_URL, USER_DATA } from '../consts/consts';
import { STATUS_ACTIVE, User, UserData } from '../model/user';
import { BehaviorSubject, tap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  URL_UPDATE_PROFILE: string = BASE_URL + "/api/user/profile";
  URL_UPDATE_PASSWORD: string = BASE_URL + "/api/user/password";

  URL_LOGIN: string = BASE_URL + "/api/auth/login";
  URL_FORGOT_PASSWORD: string = BASE_URL + "/api/auth/recoverpassword";
  URL_NEW_PASSWORD: string = BASE_URL + "/api/auth/passwordreset";
  URL_REGISTER_USER: string = BASE_URL + "/api/auth/userregistration";
  URL_ACTIVATE_USER: string = BASE_URL + "/api/auth/useractivation";
  URL_REQUEST_USER_ACTIVATION: string = BASE_URL + "/api/auth/useractivationrequest";

  user = new BehaviorSubject<User | null>(null);

  timer: any = null;

  constructor(private http: HttpClient, private router: Router) { }

  updateUserProfile(firstName: string, lastName: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.put(this.URL_UPDATE_PROFILE, { "firstName": firstName, "lastName": lastName }, { headers })
    .pipe(tap(value => {
      let user = this.user.value;
      user!.lastName = lastName;
      user!.name = firstName;
      this.user.next(user);
    }));
  }

  updateUserPassword(password: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.put(this.URL_UPDATE_PASSWORD, { "password": password }, { headers });
  }

  login(email: string, password: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_LOGIN, { "email": email, "password": password }, { headers })
      .pipe(tap(resData => {
        this.handleAuthentication(resData as UserData);
      }));
  }

  forgotPassword(email: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_FORGOT_PASSWORD, { "email": email }, { headers });
  }

  registerNerPasswordFromCode(email: string, password: string, newPasswordCode: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_NEW_PASSWORD, { "email": email, "password": password, "newPasswordCode": newPasswordCode }, { headers })
      .pipe(tap(resData => {
        this.handleAuthentication(resData as UserData);
      }));
  }

  registerUser(email: string, password: string, firstName: string, lastName: string | null) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_REGISTER_USER, { "email": email, "password": password, "firstName": firstName, "lastName": lastName }, { headers });
  }

  requestUserActivation(email: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_REQUEST_USER_ACTIVATION, { "email": email }, { headers });
  }

  activateUser(email: string, activationCode: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_ACTIVATE_USER, { "email": email, "activationCode": activationCode }, { headers })
      .pipe(tap(resData => {
        this.handleAuthentication(resData as UserData);
      }));
  }

  isUserLoggedIn() {
    let user: User | null = this.getUserData();
    return this.isUserParamLoggedIn(user);
  }

  isUserParamLoggedIn(user: User | null): boolean {
    return (user != null && user.jwt != null && user.jwt.length > 0 && user.tokenExpirationDate && user.tokenExpirationDate > new Date().getTime()) as boolean;
  }

  getUserData() {
    let user: User | null = this.user.value;
    if (!user || user === null) {
      user = this.getUserInMemory();
    }
    if (user && user.jwt && user.jwt.length > 0 && user.tokenExpirationDate && user.tokenExpirationDate > new Date().getTime()) {
      this.user.next(user);
    } else {
      return null;
    }
    return user;
  }

  getUserId() {
    let user: User | null = this.getUserData();
    return user !== null ? user.id : null;
  }

  handleAuthentication(userData: UserData) {
    const expirationDate = new Date().getTime() + userData.expiresIn;

    let user: User = {
      email: userData.email,
      name: userData.name,
      id: userData.id,
      status: userData.status,
      jwt: userData.jwt,
      lastName: userData.lastName,
      tokenExpirationDate: expirationDate
    };
    if (user.status === STATUS_ACTIVE) {
      sessionStorage.setItem(USER_DATA, JSON.stringify(user));
      this.loadUserInMemory();
      this.autoLogout(userData.expiresIn);
    }
  }

  autoLogout(expiresIn: number) {
    if (this.timer !== null) { clearTimeout(this.timer); }
    this.timer = setTimeout(() => { this.logout(); this.router.navigate(['/login']); }, expiresIn);
  }

  loadUserInMemory() {
    let user = this.getUserInMemory();
    if (user && user.jwt && user.jwt.length > 0 && user.tokenExpirationDate && user.tokenExpirationDate > new Date().getTime()) {
      this.user.next(user);
    }
  }

  getUserInMemory() {
    let data = sessionStorage.getItem(USER_DATA)
    if (data === null) {
      return null
    }
    let user: User = JSON.parse(data);
    return user;
  }

  logout() {
    sessionStorage.removeItem(USER_DATA);
    this.user.next(null);
  }

  isPasswordParametersValid(password: string): boolean {
    if (password === null || password === '' || !/\d/.test(password) || password.length < 6) {
      return false;
    }
    return true;
  }

  updateUserInfo(firstName: string, lastName: string) {
    let user: User | null = this.user.value;
    if (!user || user === null) {
      user = this.getUserInMemory();
    }
    if (user && user.jwt && user.jwt.length > 0 && user.tokenExpirationDate && user.tokenExpirationDate > new Date().getTime()) {
      this.user.value!.name = firstName;
      this.user.value!.lastName = lastName;
      this.user.next(user);
    } else {
      this.logout();
      this.router.navigate(['/login']);
    }
  }
}
