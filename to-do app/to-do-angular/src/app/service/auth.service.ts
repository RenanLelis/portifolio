import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { User, UserData } from '../model/user';
import { BASE_URL, USER_DATA } from './consts';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  URL_LOGIN: string = BASE_URL + "/api/auth/login";
  URL_FORGOT_PASSWORD: string = BASE_URL + "/api/auth/recoverpassword";
  URL_NEW_PASSWORD: string = BASE_URL + "/api/auth/passwordreset";
  URL_REGISTER_USER: string = BASE_URL + "/api/auth/userregistration";
  URL_ACTIVATE_USER: string = BASE_URL + "/api/auth/useractivation";

  user = new BehaviorSubject<User | null>(null);

  constructor(private http: HttpClient) { }

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
      this.login(email, password).subscribe();
    }));
  }

  registerUser(email: string, password: string, userName: string, lastName: string | null) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.post(this.URL_REGISTER_USER, { "email": email, "password": password, "userName": userName, "lastName": lastName }, { headers })
    .pipe(tap(resData => {
      this.handleAuthentication(resData as UserData);
    }));
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
      this.logout();
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
    sessionStorage.setItem(USER_DATA, JSON.stringify(user));
    this.loadUserInMemory();
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




}
