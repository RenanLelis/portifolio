import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage } from 'src/app/message/message';
import { TaskList, createDefaultTaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-lists-view',
  templateUrl: './lists-view.component.html',
  styleUrls: ['./lists-view.component.css']
})
export class ListsViewComponent implements OnInit {

  taskLists: TaskList[] | null = null;
  selectedTaskList: TaskList | null = createDefaultTaskList();
  @Output() onSelectTaskList: EventEmitter<TaskList> = new EventEmitter();
  @Output() onShowAllTasks: EventEmitter<any> = new EventEmitter();


  loading: boolean = false;
  errorMessage: string = "";
  suscessMessage: string = "";

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {
    if ((!this.taskService.showAllTasks) && this.taskService.selectedTaskList !== null && this.taskService.selectedTaskList.id !== null && this.taskService.selectedTaskList.id > 0) {
      this.selectedTaskList = this.taskService.selectedTaskList;
    }
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
    } else {

    }
  }

  newTaskList() {
    this.router.navigate(['/listedit']);
  }

  showAllMyTasks() {
    this.taskService.showAllTasks = true;
    this.onShowAllTasks.emit();
  }

  selectTaskList(taskList: TaskList) {
    this.taskService.showAllTasks = false;
    this.taskService.selectedTaskList = taskList;
    this.selectedTaskList = taskList;
    this.onSelectTaskList.emit(taskList);
  }

  openEditTaskListScreen(idList: number) {
    this.router.navigate([`/listedit/${idList}`]);
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

  closeMessage() {
    this.suscessMessage = "";
  }

}
