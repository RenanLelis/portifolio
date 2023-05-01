import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskList } from 'src/app/model/taskList';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/msg/messages';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-list-edit',
  templateUrl: './list-edit.component.html',
  styleUrls: ['./list-edit.component.css']
})
export class ListEditComponent implements OnInit {

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
      let list: TaskList | null = this.getList(this.id);
      if (list != null) {
        this.formTaskList.patchValue({
          'listName': list.listName,
          'listDescription': list.listDescription
        });
      }
    }
  }

  getList(id: number): TaskList | null {
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
          if (newTaskList && newTaskList !== null) {
            this.taskService.selectTaskList(newTaskList as TaskList);
          }
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
