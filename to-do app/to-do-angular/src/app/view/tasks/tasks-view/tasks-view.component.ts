import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage } from 'src/app/message/message';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-tasks-view',
  templateUrl: './tasks-view.component.html',
  styleUrls: ['./tasks-view.component.css'],
})
export class TasksViewComponent implements OnInit {
  private _showAllTasks: boolean = false;
  @Input() set showAllTasks(value: boolean) {
    this._showAllTasks = value;
    this.getTasks();
  }
  get showAllTasks(): boolean {
    return this._showAllTasks;
  }

  private _selectedTaskList: TaskList | null = null;
  @Input() set selectedTaskList(value: TaskList | null) {
    this._selectedTaskList = value;
    this.getTasks();
  }
  get selectedTaskList(): TaskList | null {
    return this._selectedTaskList;
  }

  tasks: Task[] = [];
  showConfirmUncompleteTasks: boolean = false;
  showConfirmCompleteTasks: boolean = false;
  showConfirmDelete: boolean = false;
  loading: boolean = false;
  errorMessage: string = '';
  suscessMessage: string = '';

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void { }

  getTasks() {
    if (this.showAllTasks) {
      this.tasks = this.taskService.getAllTasks();
    } else if (this.selectedTaskList !== null) {
      this.taskService.fetchTasksByList(this.selectedTaskList.id).subscribe({
        next: (value) => {
          this.loading = false;
          this.tasks = value as Task[];
        },
        error: (e) => {
          console.log(e);
          this.loading = false;
          if (e.error && e.error.errorMessage) {
            this.errorMessage = getMessage(e.error.errorMessage);
          } else {
            this.errorMessage = getErrorMessage();
          }
        },
      });
    }
  }

  newTask() {
    // this.router.navigate(['/taskedit']);
  }

  openEditTaskListScreen() {
    this.router.navigate([`/listedit/${this.selectedTaskList!.id}`]);
  }

  editTask(taskId: number) {
    // this.router.navigate([`/taskedit/${taskId}`]);
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
    this.errorMessage = '';
  }

  closeMessage() {
    this.suscessMessage = '';
  }
}
