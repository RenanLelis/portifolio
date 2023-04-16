import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  @Output() onMenuClick: EventEmitter<any> = new EventEmitter();

  menuClick() {
    this.onMenuClick.emit();
  }

}
