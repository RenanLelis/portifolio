import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  showMenu:boolean = false;

  onMenuClick() {
    this.showMenu = !this.showMenu;
  }

  closeMenu() {
    this.onMenuClick();
  }

}
