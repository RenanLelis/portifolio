import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Task } from 'src/app/model/task';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-task-edit',
  templateUrl: './task-edit.component.html',
  styleUrls: ['./task-edit.component.css']
})
export class TaskEditComponent implements OnInit {

  id: number | null = this.route.snapshot.params['id'];
  loading: boolean = false;
  errorMessage: string = "";

  formTask: FormGroup = new FormGroup({
    "taskName": new FormControl('', [Validators.required]),
    "taskDescription": new FormControl(''),
    "taskDeadline": new FormControl(''),
  });

  constructor(private router: Router, private route: ActivatedRoute, private taskService: TaskService) { }

  ngOnInit(): void {
    if (this.id !== null && this.id > 0) {
      let task: Task | null = this.getTask(this.id);
      if (task !== null) {
        this.formTask.patchValue({
          'taskName': task.taskName,
          'taskDescription': task.taskDescription,
          'taskDeadline': task.deadline
        });
      }
    }
  }

  getTask(id: number): Task | null {
    return this.taskService.getTask(id);
  }

  cancel() {
    this.navigateBackToTheList();
  }

  navigateBackToTheList() {
    this.id = null;
    this.formTask.reset();
    this.router.navigate(['/home/']);
  }

  save() {
    if (this.formTask.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      let subscription;
      if (this.id && this.id !== null && this.id > 0) {
        subscription = this.taskService.updateTask(this.id, this.formTask.value.taskName, this.formTask.value.taskDescription, this.formTask.value.taskDeadline);
      } else {
        let listId = 
          this.taskService.selectedList.value != null && this.taskService.selectedList.value.id != null && this.taskService.selectedList.value.id > 0 
          ? this.taskService.selectedList.value!.id : 0;
        subscription = this.taskService.createTask(this.formTask.value.taskName, this.formTask.value.taskDescription, this.formTask.value.taskDeadline, listId);
      }
      subscription.subscribe({
        next: (newTask) => {
          this.loading = false;
          this.navigateBackToTheList();
        },
        error: (e) => { this.handleError(e) }
      });
    }
  }

  handleError(e: any) {
    this.loading = false;
    if (e.error && e.error.errorMessage) {
      this.errorMessage = getMessage(e.error.errorMessage);
    } else {
      this.errorMessage = getErrorMessage();
    }
  }

  closeErrorMessage() {
    this.errorMessage = "";
  }

}
