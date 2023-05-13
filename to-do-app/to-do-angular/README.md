# TO-DO-Angular

Front-end em Angular para o projeto

Projeto implementado de forma a ter views (components) que se comunicam com services, sendo os services responsáveis por manter os dados (state management) com uso de observables.

Aqui um exemplo de View, login component
```
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  formLogin: FormGroup = new FormGroup({
    "email": new FormControl('', [Validators.email, Validators.required]),
    "password": new FormControl('', [Validators.required, passwordValidation]),
  });

  loading: boolean = false;
  errorMessage: string = "";

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
      if (this.userService.isUserLoggedIn()) {
        this.router.navigate(['/']);
      }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  login() {
    if (this.formLogin.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      this.userService.login(this.formLogin.get('email')!.value.trim(), this.formLogin.get('password')!.value.trim()).subscribe({
        next: (userData) => { 
          this.loading = false; 
          this.userLoginHandler(userData as UserData) 
        },
        error: (err) => {
          this.loading = false;
          this.handleError(err);
        }
      });
    }
  }

  userLoginHandler(userData: UserData) {
    this.loading = false;
    if ((userData).status === STATUS_ACTIVE) {
      this.router.navigate(['/']);
    } else {
      this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]);
    }
  }

  handleError(e: any) {
    console.log(e);
    if (e.error && e.error.errorMessage) {
      if (e.error.errorMessage === "MSE07") { this.router.navigate(['/useractivationrequest/' + this.formLogin.get('email')!.value.trim()]); }
      else { this.errorMessage = getMessage(e.error.errorMessage); }
    } else {
      this.errorMessage = getErrorMessage();
    }
  }


}

```
Nesse componente é realizada a comunicação com um service responsável pelas operações com usuário e gestão da autenticação.

```
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
```

Além disso foi pensado em um interceptor para adicionar o header de autenticação, caso exista, a cada requisição HTTP e um filtro de rotas caso seja necessário usuário autenticado para acesso a determinada rota ou página.

```
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
            // console.log(resData);
            if (resData instanceof HttpResponse && (<HttpResponse<any>>resData).headers && (<HttpResponse<any>>resData).headers.get('AUTH')) {
              let header = (<HttpResponse<any>>resData).headers.get('AUTH')
              if (header != null) {
                try {
                  let newUser: UserData = JSON.parse(header!);
                  this.userService.handleAuthentication(newUser);
                }
                catch (e) {
                  console.log(e);
                }

              }
            }
          }))
    } else {
      return next.handle(req);
    }
  }
}

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(private userService: UserService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, router: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (!this.userService.isUserLoggedIn()) {
      return this.router.createUrlTree(['/login']);
    } else {
      return true;
    }
  }
}
```

## Development server

Run `ng serve` for a dev server. Navigate to `http://localhost:4200/`. The application will automatically reload if you change any of the source files.

## Code scaffolding

Run `ng generate component component-name` to generate a new component. You can also use `ng generate directive|pipe|service|class|guard|interface|enum|module`.

## Build

Run `ng build` to build the project. The build artifacts will be stored in the `dist/` directory.

## Running unit tests

Run `ng test` to execute the unit tests via [Karma](https://karma-runner.github.io).

## Running end-to-end tests

Run `ng e2e` to execute the end-to-end tests via a platform of your choice. To use this command, you need to first add a package that implements end-to-end testing capabilities.

## Further help

To get more help on the Angular CLI use `ng help` or go check out the [Angular CLI Overview and Command Reference](https://angular.io/cli) page.
