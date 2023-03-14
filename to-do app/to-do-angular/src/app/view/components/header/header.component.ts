import { Component } from '@angular/core';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  showMenu: boolean = false
  isUserLoggedIn: boolean = false
  user: User | null= null;

  constructor(private authService: AuthService) {
    this.authService.user.subscribe(value => {
      this.user = value;
      this.isUserLoggedIn = this.authService.isUserParamLoggedIn(this.user)
    })
  }

  onMenuClick() {
    this.showMenu = !this.showMenu;
  }

}
