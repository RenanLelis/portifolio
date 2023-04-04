import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-success-message',
  templateUrl: './success-message.component.html',
  styleUrls: ['./success-message.component.css']
})
export class SuccessMessageComponent {

  @Input() message = "";
  @Output() onClose: EventEmitter<any> = new EventEmitter();

  close() {
    this.message = "";
    this.onClose.emit();
  }

}
