import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage } from 'src/app/message/message';
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

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    this.taskService.lists.subscribe(value => { this.taskLists = value });
    this.taskService.selectedList.subscribe(value => { this.selectedTaskList = value; });
    this.taskService.isSetShowAllTasks.subscribe(value => { this.isSetShowAllTasks = value });
    this.taskService.tasks.subscribe(value => { this.tasks = value });
  }

  dropMenuList() {
    this.showMenuList = !this.showMenuList;
  }

  closeMenuList() {
    this.showMenuList = false;
  }

  // ----------------------------------------------

  newList() {
    this.router.navigate([`/home/list`]);
  }

  editList() {
    if (this.selectedTaskList !== null) {
      this.router.navigate([`/home/list/${this.selectedTaskList!.id}`]);
    }
  }

  deleteList() {
    //TODO
  }

  confirmDeleteList() {
    //TODO
  }

  completeList() {
    //TODO
  }

  uncompleteList() {
    //TODO
  }

  // ----------------------------------------------

  newTask() {
    //TODO redirect to new task page, set the list as the selected list, if null set as default list
  }

  editTask(task: Task) {
    //TODO redirect to edit task page
  }

  deleteTask(taskID: number) {
    //TODO
  }

  completeTask(taskID: number) {
    this.loading = true;
    this.taskService.completeTask(taskID).subscribe({
      next: (value) => { this.loading = false; },
      error: (err) => { this.handleError(err); }
    });
  }

  uncompleteTask(taskID: number) {
    this.loading = true;
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


}
