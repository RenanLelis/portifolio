import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage } from 'src/app/message/message';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/services/task.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

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
    this.getTasksLists();
    this.taskService.lists.subscribe(value => { this.taskLists = value });
    this.taskService.selectedList.subscribe(value => { this.selectedTaskList = value });
    this.taskService.isSetShowAllTasks.subscribe(value => { this.isSetShowAllTasks = value });
    this.taskService.tasks.subscribe(value => { this.tasks = value });
  }

  getTasksLists() {
    this.taskService.fetchTasksAndLists().subscribe({
      next: (resData) => {
        console.log(resData);
        this.taskLists = resData as TaskList[];
      },
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
    //TODO
    this.router.navigate(['/home/list'])
  }

  onMenuClick() {
    this.showMenu = !this.showMenu;
  }

  showAllTasks() {
    this.taskService.showAllTasks();
    this.onMenuClick();
  }

  selectTaskList(selectedTaskList: TaskList) {
    this.taskService.selectTaskList(selectedTaskList);
    this.onMenuClick();
  }

}
