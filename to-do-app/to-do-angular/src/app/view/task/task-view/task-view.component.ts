import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { getErrorMessage, getMessage, getMessageConfirmCompleteList, getMessageConfirmDelete, getMessageConfirmUncompleteList } from 'src/app/msg/messages';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-task-view',
  templateUrl: './task-view.component.html',
  styleUrls: ['./task-view.component.css']
})
export class TaskViewComponent implements OnInit {

  selectedList: TaskList | null = null;
  isSetShowAllTasks: boolean = false;
  tasks: Task[] = [];
  lists: TaskList[] = [];

  exibTasks: boolean = true;

  showListSelector: boolean = false;
  selectedTask: number | null = null;
  showMenuList: boolean = false;
  loading: boolean = false;
  errorMessage: string = "";
  confirmMessageDelete: string = "";
  confirmMessageCompleteList: string = "";
  confirmMessageUncompleteList: string = "";

  constructor(private taskService: TaskService, private router: Router) { }

  getListsToSelect() {
    if (this.selectedList === null) { return this.lists }
    return this.lists.filter(list => list.id.toString() !== this.selectedList!.id.toString())
  }

  ngOnInit(): void {
    this.taskService.isSetShowAllTasks.subscribe(value => { this.isSetShowAllTasks = value });
    this.taskService.selectedList.subscribe(value => {
      const oldId = this.selectedList != null ? this.selectedList.id : 0;
      this.selectedList = value;
      if (value !== null && (this.selectedList === null || value.id.toString() !== oldId.toString())) {
        this.taskService.fetchTasksByList(value.id).subscribe({
          error: (err) => { this.handleError(err); }
        });
      }
    });
    this.taskService.tasks.subscribe(value => { this.tasks = value; });
    this.taskService.lists.subscribe(value => { this.lists = value; });
  }

  public trackItem(index: number, item: Task) {
    return item.id;
  }

  dropMenuList(event: any) {
    event.stopPropagation();
    this.showMenuList = !this.showMenuList;
  }

  closeMenuList() {
    if (this.showMenuList) {
      this.showMenuList = false;
    }
  }

  // ----------------------------------------------

  newList() {
    this.closeMenuList();
    this.router.navigate([`/home/list`]);
  }

  editList() {
    this.closeMenuList();
    if (this.selectedList !== null) {
      this.router.navigate([`/home/list/${this.selectedList!.id}`]);
    }
  }

  deleteList() {
    this.closeMenuList();
    this.confirmMessageDelete = getMessageConfirmDelete();
  }

  confirmDeleteList() {
    this.closeConfirmMessageDelete()
    if (this.selectedList !== null && this.selectedList!.id !== null) {
      this.loading = true;
      this.taskService.deleteTaskList(this.selectedList!.id!).subscribe({
        next: (value) => { this.loading = false; },
        error: (err) => { this.handleError(err) }
      });
    }
  }

  completeList() {
    this.closeMenuList();
    this.confirmMessageCompleteList = getMessageConfirmCompleteList();
  }

  confirmCompleteList() {
    this.closeConfirmMessageCompleteList()
    if (this.selectedList !== null) {
      this.loading = true;
      this.exibTasks = false;
      this.taskService.completeTasksFromList(this.selectedList!.id).subscribe({
        next: (value) => { this.loading = false; this.exibTasks = true; },
        error: (err) => { this.handleError(err) }
      });
    }
  }

  uncompleteList() {
    this.closeMenuList();
    this.confirmMessageUncompleteList = getMessageConfirmUncompleteList();
  }

  confirmUncompleteList() {
    this.closeConfirmMessageUncompleteList()
    if (this.selectedList !== null) {
      this.loading = true;
      this.exibTasks = false;
      this.taskService.uncompleteTasksFromList(this.selectedList!.id).subscribe({
        next: (value) => { this.loading = false; this.exibTasks = true; },
        error: (err) => { this.handleError(err) }
      });
    }
  }

  moveTasksFromList(listOrigin: TaskList, listDestiny: TaskList) {
    this.closeListSelector();
    this.loading = true;
    this.exibTasks = false;
    this.taskService.moveTasksFromList(listOrigin.id, listDestiny.id).subscribe({
      next: (value) => { this.handleResponse(); },
      error: (err) => { this.handleError(err) }
    });
  }

  // ----------------------------------------------

  newTask() {
    this.router.navigate([`/home/task`]);
  }

  editTask(task: Task) {
    this.router.navigate([`/home/task/${task.id}`]);
  }

  deleteTask(taskID: number) {
    this.loading = true;
    this.taskService.deleteTask(taskID).subscribe({
      next: (value) => { this.loading = false; },
      error: (err) => { this.handleError(err); }
    });
  }

  completeTask(taskID: number) {
    this.taskService.completeTask(taskID).subscribe({
      next: (value) => { this.handleResponse(); },
      error: (err) => { this.handleError(err); }
    });
  }

  uncompleteTask(taskID: number) {
    this.taskService.uncompleteTask(taskID).subscribe({
      next: (value) => { this.handleResponse(); },
      error: (err) => { this.handleError(err); }
    });
  }

  showMoveTaskOption(taskID: number | null) {
    this.selectedTask = taskID;
    this.showListSelector = true;
  }

  moveTaskToList(task: number, list: TaskList) {
    this.closeListSelector();
    this.loading = true;
    this.exibTasks = false;
    this.taskService.moveTaskToList(task, list.id).subscribe({
      next: (value) => { this.handleResponse(); },
      error: (err) => { this.handleError(err) }
    });
  }

  selectListToMoveTask(list: TaskList) {
    if (this.selectedTask !== null) { this.moveTaskToList(this.selectedTask, list); }
    else { this.moveTasksFromList(this.selectedList!, list) }
  }

  // ----------------------------------------------

  handleResponse() {
    this.loading = false; 
    this.exibTasks = true;
  }

  handleError(e: any) {
    this.loading = false;
    this.exibTasks = true;
    console.log(e);
    if (e.error && e.error.errorMessage) {
      this.errorMessage = getMessage(e.error.errorMessage);
    } else {
      this.errorMessage = getErrorMessage();
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  closeConfirmMessageDelete() {
    this.confirmMessageDelete = "";
  }

  closeConfirmMessageCompleteList() {
    this.confirmMessageCompleteList = "";
  }

  closeConfirmMessageUncompleteList() {
    this.confirmMessageUncompleteList = "";
  }

  closeListSelector() {
    this.showListSelector = false;
    this.selectedTask = null;
  }


}
