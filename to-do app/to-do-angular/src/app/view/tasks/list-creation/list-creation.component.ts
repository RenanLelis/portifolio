import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { getErrorMessageInputValues } from 'src/app/message/message';
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
      //TODO Save and update taskLists on the service
    }
  }


}
