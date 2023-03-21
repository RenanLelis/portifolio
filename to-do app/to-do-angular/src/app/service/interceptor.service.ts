import { HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { tap } from 'rxjs';
import { User, UserData } from '../model/user';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    if (this.authService.isUserLoggedIn()) {
      let user: User = this.authService.user.value!
      const modifiedReq = req.clone({ headers: new HttpHeaders({ 'AUTH': user.jwt }) });
      return next.handle(modifiedReq)
        .pipe(
          tap(resData => {
            if (resData instanceof HttpResponse && (<HttpResponse<any>>resData).headers && (<HttpResponse<any>>resData).headers.get('AUTH')) {
              let header = (<HttpResponse<any>>resData).headers.get('AUTH')
              if (header != null) {
                try {
                  let newUser: UserData = JSON.parse(header!);
                  this.authService.handleAuthentication(newUser);
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
