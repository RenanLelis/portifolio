import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { getErrorMessage, getMessage } from 'src/app/msg/messages';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  taskLists: TaskList[] = [];
  selectedTaskList: TaskList | null = null;
  isSetShowAllTasks: boolean = false;
  tasks: Task[] = [];

  showMenu: boolean = false;
  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    if (this.taskService.selectedList.value === null && !this.isSetShowAllTasks
      && this.taskService.lists.value !== null && this.taskService.lists.value.length > 0) {
      this.taskService.selectTaskList(this.taskService.lists.value[0]);
    }
    this.taskService.lists.subscribe(value => { this.taskLists = value });
    this.taskService.selectedList.subscribe(value => { this.selectedTaskList = value });
    this.taskService.isSetShowAllTasks.subscribe(value => { this.isSetShowAllTasks = value });
    this.taskService.tasks.subscribe(value => { this.tasks = value });
    this.getTasksLists();
  }

  getTasksLists() {
    this.taskService.fetchTasksAndLists().subscribe({
      next: (resData) => { },
      error: (err) => { this.handleError(err) }
    });
  }

  handleError(err: any) {
    console.log(err);
    if (err.error && err.error.errorMessage) {
      this.errorMessage = getMessage(err.error.errorMessage);
    } else {
      this.errorMessage = getErrorMessage();
    }
  }

  onNewTaskList() {
    this.onMenuClick();
    this.router.navigate(['/home/list']);
  }

  onMenuClick() {
    this.showMenu = !this.showMenu;
    if (this.showMenu) {
      this.getTasksLists();
    }
  }

  showAllTasks() {
    this.taskService.setShowAllTasks();
    this.onMenuClick();
    this.router.navigate(['/home'])
  }

  selectTaskList(selectedTaskList: TaskList) {
    this.taskService.selectTaskList(selectedTaskList);
    this.onMenuClick();
    this.router.navigate(['/home'])
  }

}