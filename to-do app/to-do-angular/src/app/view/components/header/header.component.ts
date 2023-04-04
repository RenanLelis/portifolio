import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  isUserLoggedIn: boolean = false
  user: User | null= null;

  showMenu:boolean = false;

  onMenuClick() {
    this.showMenu = !this.showMenu;
  }

  closeMenu() {
    this.onMenuClick();
  }

  constructor(private authService: AuthService) {
    this.authService.user.subscribe(value => {
      this.user = value;
      this.isUserLoggedIn = this.authService.isUserParamLoggedIn(this.user)
    })
  }

}
