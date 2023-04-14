import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { getErrorMessage, getMessage, getMessageConfirmCompleteList, getMessageConfirmDelete, getMessageConfirmUncompleteList } from 'src/app/message/message';
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

  @Output() onDelete: EventEmitter<any> = new EventEmitter();

  tasks: Task[] = [];
  uncompleteTasksMessage: string = "";
  completeTasksMessage: string = "";
  deleteMessage: string = "";
  loading: boolean = false;
  errorMessage: string = '';
  suscessMessage: string = '';

  constructor(private taskService: TaskService, private router: Router) { }

  ngOnInit(): void {

  }

  getTasks() {
    if (this.showAllTasks) {
      this.tasks = this.taskService.getAllTasks();
    } else if (this.selectedTaskList !== null) {
      this.taskService.fetchTasksByList(this.selectedTaskList.id).subscribe({
        next: (value) => {
          this.loading = false;
          this.tasks = value as Task[];
        },
        error: (e) => { this.errorHandler(e); },
      });
    }
  }

  newTaskList() {
    this.router.navigate(['/listedit']);
  }

  openEditTaskListScreen() {
    this.router.navigate([`/listedit/${this.selectedTaskList!.id}`]);
  }

  newTask() {
    //TODO
    // this.router.navigate(['/taskedit']);
  }

  editTask(taskId: number) {
    //TODO
    // this.router.navigate([`/taskedit/${taskId}`]);
  }

  deleteTaskList() {
    this.deleteMessage = getMessageConfirmDelete();
  }

  confirmDeletionTaskList() {
    let listID = this.selectedTaskList!.id !== null ? this.selectedTaskList!.id! : 0;
    this.loading = true;
    this.taskService.deleteTaskList(listID).subscribe({
      next: (value) => {
        this.loading = false;
        this.closeConfirmationDelete();
        this.onDelete.emit();
      },
      error: (err) => { this.errorHandler(err) },
    });
  }

  completeTaskList() {
    this.completeTasksMessage = getMessageConfirmCompleteList();
  }

  confirmCompletionTaskList() {
    let listID = this.selectedTaskList!.id !== null ? this.selectedTaskList!.id! : 0;
    this.loading = true;
    this.taskService.completeTasksFromList(listID).subscribe({
      next: (value) => {
        this.loading = false;
        this.tasks = this.taskService.getTasksByList(listID);
        this.closeConfirmationCompleteList();
      },
      error: (err) => { this.errorHandler(err) },
    });
  }

  uncompleteTaskList() {
    this.uncompleteTasksMessage = getMessageConfirmUncompleteList();
  }

  confirmUncompletionTaskList() {
    let listID = this.selectedTaskList!.id !== null ? this.selectedTaskList!.id! : 0;
    this.loading = true;
    this.taskService.uncompleteTasksFromList(listID).subscribe({
      next: (value) => {
        this.loading = false;
        this.tasks = this.taskService.getTasksByList(listID);
        this.closeConfirmationUncompleteList();
      },
      error: (err) => { this.errorHandler(err) },
    });
  }

  closeConfirmationDelete() {
    this.deleteMessage = ""
  }

  closeConfirmationCompleteList() {
    this.completeTasksMessage = "";
  }

  closeConfirmationUncompleteList() {
    this.uncompleteTasksMessage = "";
  }

  closeErrorMessage() {
    this.errorMessage = '';
  }

  closeMessage() {
    this.suscessMessage = '';
  }

  errorHandler(e: any) {
    console.log(e);
    this.loading = false;
    if (e.error && e.error.errorMessage) { this.errorMessage = getMessage(e.error.errorMessage); }
    else { this.errorMessage = getErrorMessage(); }
  }
}
