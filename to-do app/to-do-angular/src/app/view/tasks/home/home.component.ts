import { Component, OnInit } from '@angular/core';
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

  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";

  constructor(private taskService: TaskService) { }

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
    if (newTasksLists !== null && newTasksLists.length > 0) {
      newTasksLists = newTasksLists.filter(taskLists => {
        return (taskLists.id !== null && taskLists.id > 0)
      });
      taskLists.push(...newTasksLists);
    }
    taskLists.push(defaultTaskList);
    this.taskLists = taskLists;
    if (this.selectedTaskList === null) { this.selectedTaskList = defaultTaskList; }
  }

  selectTaskList(idList: number | null) {
    //TODO
  }

  openEditTaskListScreen() {
    //TODO - use selected list
  }

  openCreateNewTaskListScreen() {
    //TODO
  }

  deleteTaskList() {
    //TODO - Confirm first
  }

  confirmDeletionTaskList() {
    //TODO
  }

  completeTaskList() {
    //TODO - Confirm first
  }

  confirmCompletionTaskList() {
    //TODO
  }

  uncompleteTaskList() {
    //TODO - Confirm first
  }

  confirmUncompletionTaskList() {
    //TODO
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  closeMessage() {
    this.suscessMessage = "";
  }

}
