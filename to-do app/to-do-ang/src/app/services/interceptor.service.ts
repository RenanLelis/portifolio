import { Injectable } from '@angular/core';
import { HttpHandler, HttpHeaders, HttpRequest, HttpResponse } from '@angular/common/http';
import { User, UserData } from '../model/user';
import { tap } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService {

  constructor(private userService: UserService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (this.userService.isUserLoggedIn()) {
      let user: User = this.userService.user.value!
      const modifiedReq = req.clone({ headers: new HttpHeaders({ 'AUTH': user.jwt }) });
      return next.handle(modifiedReq)
        .pipe(
          tap(resData => {
            if (resData instanceof HttpResponse && (<HttpResponse<any>>resData).headers && (<HttpResponse<any>>resData).headers.get('AUTH')) {
              let header = (<HttpResponse<any>>resData).headers.get('AUTH')
              if (header != null) {
                try {
                  let newUser: UserData = JSON.parse(header!);
                  this.userService.handleAuthentication(newUser);
                }
                catch (e) {
                  console.log(e);
                  //TODO handle error
                }

              }
            }
          }))
    } else {
      return next.handle(req);
    }
  }
}
