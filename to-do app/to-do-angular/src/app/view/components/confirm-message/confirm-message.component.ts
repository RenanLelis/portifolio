import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-message',
  templateUrl: './confirm-message.component.html',
  styleUrls: ['./confirm-message.component.css']
})
export class ConfirmMessageComponent {

  @Input() message = "";
  @Output() onClose: EventEmitter<any> = new EventEmitter();
  @Output() onConfirm: EventEmitter<any> = new EventEmitter();

  close() {
    this.message = "";
    this.onClose.emit();
  }

  confirm() {
    this.message = "";
    this.onConfirm.emit();
  }

}
