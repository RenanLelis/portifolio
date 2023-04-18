import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { Task } from 'src/app/model/task';
import { TaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/services/task.service';

@Component({
  selector: 'app-new-list',
  templateUrl: './new-list.component.html',
  styleUrls: ['./new-list.component.css']
})
export class NewListComponent implements OnInit {

  id: number | null = this.route.snapshot.params['id'];
  loading: boolean = false;
  errorMessage: string = "";

  formTaskList: FormGroup = new FormGroup({
    "listName": new FormControl('', [Validators.required]),
    "listDescription": new FormControl(''),
  });

  constructor(private router: Router, private route: ActivatedRoute, private taskService: TaskService) { }

  ngOnInit(): void {
      if (this.id !== null && this.id > 0) {
        let list: TaskList = this.getList(this.id);
        this.formTaskList.patchValue({
          'listName': list.listName,
          'listDescription': list.listDescription
        });
      }
  }

  getList(id: number): TaskList {
    return this.taskService.getList(id);
  }

  cancel() {
    this.id = null;
    this.formTaskList.reset();
    this.router.navigate(['/home/']);
  }

  save() {
    if (this.formTaskList.invalid) {
      this.errorMessage = getErrorMessageInputValues();
    } else {
      this.loading = true;
      let subscription;
      if (this.id && this.id !== null && this.id > 0) {
        subscription = this.taskService.updateTaskList(this.formTaskList.value.listName, this.formTaskList.value.listDescription, this.id);
      } else {
        subscription = this.taskService.createTaskList(this.formTaskList.value.listName, this.formTaskList.value.listDescription);
      }
      subscription.subscribe({
        next: (newTaskList) => {
          console.log(newTaskList);
          this.taskService.selectTaskList(newTaskList as TaskList);
          this.loading = false;
          this.router.navigate(['/home/']);

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
