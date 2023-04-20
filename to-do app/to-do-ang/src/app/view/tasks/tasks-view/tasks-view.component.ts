import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage, getMessageConfirmCompleteList, getMessageConfirmDelete, getMessageConfirmUncompleteList } from 'src/app/message/message';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/services/task.service';

@Component({
  selector: 'app-tasks-view',
  templateUrl: './tasks-view.component.html',
  styleUrls: ['./tasks-view.component.css']
})
export class TasksViewComponent implements OnInit {

  taskLists: TaskList[] = [];
  selectedTaskList: TaskList | null = null;
  isSetShowAllTasks: boolean = false;
  tasks: Task[] = [];

  showMenuList: boolean = false;
  loading: boolean = false;
  errorMessage: string = "";
  confirmMessageDelete: string = "";
  confirmMessageCompleteList: string = "";
  confirmMessageUncompleteList: string = "";

  showTasksAsCards: boolean = false;

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    this.taskService.lists.subscribe(value => { this.taskLists = value });
    this.taskService.isSetShowAllTasks.subscribe(value => { this.isSetShowAllTasks = value });
    this.taskService.selectedList.subscribe(value => { 
      this.selectedTaskList = value; 
      if (this.selectedTaskList !== null) {
        this.taskService.fetchTasksByList(this.selectedTaskList.id).subscribe({
          next: (value) => {},
          error: (err) => { this.handleError(err); }
        });
      }
    });
    this.taskService.tasks.subscribe(value => { this.tasks = value });
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
    if (this.selectedTaskList !== null) {
      this.router.navigate([`/home/list/${this.selectedTaskList!.id}`]);
    }
  }

  deleteList() {
    this.closeMenuList();
    this.confirmMessageDelete = getMessageConfirmDelete();
  }

  confirmDeleteList() {
    this.closeConfirmMessageDelete()
    if (this.selectedTaskList !== null && this.selectedTaskList!.id !== null) {
      this.loading = true;
      this.taskService.deleteTaskList(this.selectedTaskList!.id!).subscribe({
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
    if (this.selectedTaskList !== null) {
      this.loading = true;
      this.taskService.completeTasksFromList(this.selectedTaskList!.id).subscribe({
        next: (value) => { this.loading = false; },
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
    if (this.selectedTaskList !== null) {
      this.loading = true;
      this.taskService.uncompleteTasksFromList(this.selectedTaskList!.id).subscribe({
        next: (value) => { this.loading = false; },
        error: (err) => { this.handleError(err) }
      });
    }
  }

  // ----------------------------------------------

  changeTaskExibition() {
    this.showTasksAsCards = !this.showTasksAsCards;
  }

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
      next: (value) => { this.loading = false; },
      error: (err) => { this.handleError(err); }
    });
  }

  uncompleteTask(taskID: number) {
    this.taskService.uncompleteTask(taskID).subscribe({
      next: (value) => { this.loading = false; },
      error: (err) => { this.handleError(err); }
    });
  }

  // ----------------------------------------------

  handleError(e: any) {
    this.loading = false;
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


}
