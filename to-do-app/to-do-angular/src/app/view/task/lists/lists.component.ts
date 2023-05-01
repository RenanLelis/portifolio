import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TaskList } from 'src/app/model/taskList';

@Component({
  selector: 'app-lists',
  templateUrl: './lists.component.html',
  styleUrls: ['./lists.component.css']
})
export class ListsComponent {

  @Input() taskLists: TaskList[] | null = null;
  @Output() onSelectTaskList: EventEmitter<TaskList> = new EventEmitter();
  @Output() onShowAllTasks: EventEmitter<any> = new EventEmitter();
  @Output() onNewTaskList: EventEmitter<any> = new EventEmitter();

  ngOnInit(): void {
  }

  newTaskList() {
    this.onNewTaskList.emit();
  }

  showAllMyTasks() {
    this.onShowAllTasks.emit();
  }

  selectTaskList(taskList: TaskList) {
    this.onSelectTaskList.emit(taskList);
  }

}