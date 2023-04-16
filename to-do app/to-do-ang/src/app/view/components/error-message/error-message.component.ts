import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-message',
  templateUrl: './error-message.component.html',
  styleUrls: ['./error-message.component.css']
})
export class ErrorMessageComponent {

  @Input() errorMessage = "";
  @Output() onClose: EventEmitter<any> = new EventEmitter();

  close() {
    this.errorMessage = "";
    this.onClose.emit();
  }

}
