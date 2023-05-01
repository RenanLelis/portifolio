import { Component, EventEmitter, Output, Input } from '@angular/core';
import { TaskList } from 'src/app/model/taskList';

@Component({
  selector: 'app-list-selector',
  templateUrl: './list-selector.component.html',
  styleUrls: ['./list-selector.component.css']
})
export class ListSelectorComponent {

  @Input() lists: TaskList[] = []
  @Output() onClose: EventEmitter<any> = new EventEmitter();
  @Output() onSelectList: EventEmitter<TaskList> = new EventEmitter();

  close() {
    this.onClose.emit();
  }

  selectList(list: TaskList) {
    this.onSelectList.emit(list);
  }

}
