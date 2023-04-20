import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { STATUS_COMPLETE, STATUS_INCOMPLETE } from 'src/app/model/task';
import { Task } from 'src/app/model/task';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {

  taskComplete: boolean = false;
  
  @Input() showTasksAsCards = false;
  @Output() onUncomplete: EventEmitter<any> = new EventEmitter();
  @Output() onCoplete: EventEmitter<any> = new EventEmitter();
  @Output() onDelete: EventEmitter<any> = new EventEmitter();
  @Output() onEdit: EventEmitter<any> = new EventEmitter();
  
  _task: Task = {id: 0, deadline: null, taskDescription: null, taskName: "", taskStatus: STATUS_INCOMPLETE};

  get task(): Task {
    return this._task;
  }

  @Input() set task(task: Task) {
    this._task = task
    this.taskComplete = this._task.taskStatus.toString() === STATUS_COMPLETE.toString();
  }

  ngOnInit(): void {
      this.taskComplete = this._task.taskStatus.toString() === STATUS_COMPLETE.toString();
  }

  changeStatusTask() {
    if (this.taskComplete) { 
      this.task.taskStatus = STATUS_INCOMPLETE;
      this.onUncomplete.emit();
    } 
    else { 
      this.task.taskStatus = STATUS_COMPLETE;
      this.onCoplete.emit();
    }
    this.taskComplete = !this.taskComplete;
  }

  delete() {
    this.onDelete.emit();
  }

  edit() {
    this.onEdit.emit();
  }

}
