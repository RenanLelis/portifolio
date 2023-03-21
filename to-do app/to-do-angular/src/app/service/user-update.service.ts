import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BASE_URL } from './consts';

@Injectable({
  providedIn: 'root'
})
export class UserUpdateService {

  URL_UPDATE_PROFILE: string = BASE_URL + "/api/user/profile";
  URL_UPDATE_PASSWORD: string = BASE_URL + "/api/user/password";

  constructor(private http: HttpClient) { }

  updateUserProfile(firstName: string, lastName: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.put(this.URL_UPDATE_PROFILE, { "firstName": firstName, "lastName": lastName }, { headers });
  }

  updateUserPassword(password: string) {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.put(this.URL_UPDATE_PASSWORD, { "password": password }, { headers });
  }


}
