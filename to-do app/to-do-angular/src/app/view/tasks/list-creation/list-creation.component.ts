import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessage, getErrorMessageInputValues, getMessage } from 'src/app/message/message';
import { TaskList } from 'src/app/model/taskList';
import { TaskService } from 'src/app/service/task.service';

@Component({
  selector: 'app-list-creation',
  templateUrl: './list-creation.component.html',
  styleUrls: ['./list-creation.component.css']
})
export class ListCreationComponent implements OnInit {

  id: number | null = this.route.snapshot.params['id'];
  loading: boolean = false;
  errorMessage: string = "";

  formTaskList: FormGroup = new FormGroup({
    "listName": new FormControl('', [Validators.required]),
    "listDescription": new FormControl(''),
  });

  constructor(private router: Router, private route: ActivatedRoute, private taskService: TaskService) { }

  ngOnInit(): void {
    if (this.taskService.taskLists.value === null || this.taskService.taskLists.value.length <= 0) {
      this.taskService.fetchTasksAndLists().subscribe({
        next: (value) => {
          this.updateForm();
        },
        error: (err) => {
          console.log(err)
          this.updateForm();
        },
      });
    } else {
      this.updateForm();
    }
  }

  updateForm() {
    if (this.id && this.id !== null && this.id! > 0) {
      const taskList = this.taskService.getTaskListById(this.id!);
      if (taskList !== null) {
        this.formTaskList.patchValue({
          'listName': taskList!.listName,
          'listDescription': taskList?.listDescription
        });
      }
    }
  }

  cancel() {
    this.id = null;
    this.formTaskList.reset();
    this.router.navigate(['/']);
  }

  save() {
    this.errorMessage = "";
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
          this.loading = false;
          console.log(newTaskList);

          this.taskService.updateLocalTaskLists(
            this.formTaskList.value.listName,
            this.formTaskList.value.listDescription,
            (this.id && this.id !== null && this.id > 0) ? this.id : (newTaskList as TaskList).id!);

          this.router.navigate(['/']);
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
  }




}
