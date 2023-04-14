import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage } from 'src/app/message/message';
import { TaskList, createDefaultTaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  taskLists: TaskList[] | null = null;
  selectedTaskList: TaskList | null = null;
  isSetShowAllTasks: boolean = false;

  showMenu: boolean = false;
  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    this.taskService.taskLists.subscribe(value => {
      this.updateTasksAndLists(value);
    });
    this.getListsAndTasks();
  }

  getListsAndTasks() {
    this.loading = true;
    this.taskService.fetchTasksAndLists().subscribe({
      next: (value) => {
        this.loading = false;
      },
      error: (e) => {
        console.log(e);
        this.loading = false;
        if (e.error && e.error.errorMessage) {
          this.errorMessage = getMessage(e.error.errorMessage);
        } else {
          this.errorMessage = getErrorMessage();
        }
      }
    });
  }

  updateTasksAndLists(newTasksLists: TaskList[]) {
    const taskLists: TaskList[] = [];
    const defaultTaskList: TaskList = createDefaultTaskList();
    taskLists.push(defaultTaskList);
    if (newTasksLists !== null && newTasksLists.length > 0) {
      newTasksLists = newTasksLists.filter(taskLists => {
        return (taskLists.id !== null && taskLists.id > 0)
      });
      taskLists.push(...newTasksLists);
    }
    this.taskLists = taskLists;
    if (this.taskService.selectedTaskList === null) {
      this.taskService.selectedTaskList = defaultTaskList;
    }
    this.selectedTaskList = this.taskService.selectedTaskList;
    this.isSetShowAllTasks = this.taskService.showAllTasks;
  }

  onMenuClick() {
    this.showMenu = !this.showMenu;
  }

  closeMenu() {
    this.showMenu = false;
  }

  selectTaskList(taskList: TaskList) {
    this.isSetShowAllTasks = false;
    this.selectedTaskList = taskList;
    this.taskService.selectedTaskList = this.selectedTaskList;
    this.taskService.showAllTasks = this.isSetShowAllTasks;
    this.closeMenu();
  }

  showAllTasks() {
    this.selectedTaskList = null;
    this.isSetShowAllTasks = true;
    this.taskService.selectedTaskList = this.selectedTaskList;
    this.taskService.showAllTasks = this.isSetShowAllTasks;
    this.closeMenu();
  }

  updateLists() {
    this.selectTaskList(createDefaultTaskList());
    this.getListsAndTasks();
  }

}
