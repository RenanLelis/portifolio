import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { TaskList } from '../model/taskList';
import { AuthService } from './auth.service';
import { BASE_URL } from './consts';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  URL_TASK_LIST = BASE_URL + "/api/taskList/";
  URL_TASK_NO_LIST = BASE_URL + "/api/taskList/nolist";
  URL_TASK_LISTS = BASE_URL + "/api/taskList/lists";
  URL_TASK_LISTS_ONLY_LISTS = BASE_URL + "/api/taskList/tasks/";
  URL_TASK_LISTS_MOVE = BASE_URL + "/api/taskList/tasks/move";

  URL_TASK = BASE_URL + "/api/task/";
  URL_TASK_COMPLETE = BASE_URL + "/api/task/complete/";
  URL_TASK_UNCOMPLETE = BASE_URL + "/api/task/uncomplete/";

  taskLists = new BehaviorSubject<TaskList[]>([]); 

  constructor(private http: HttpClient, private authService: AuthService) { this.authService.user.getValue()?.id }

  fetchTasksAndLists() {
    let headers: HttpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
    });
    return this.http.get(this.URL_TASK_LIST, { headers })
      .pipe(tap(resData => {
        console.log(resData);
        this.taskLists.next(resData as TaskList[]);
      }));
  }

  // GET /api/taskList/ - getTasksAndLists
  // GET /api/taskList/{idList} - getTasksByList
  // GET /api/taskList/nolist - getTasksUserWithoutList
  // GET /api/taskList/lists - getLists

  // POST /api/taskList/ - createTaskList {listName, listDescription}
  // PUT /api/taskList/{idList} - updateTaskList {listName, listDescription}
  // DELETE /api/taskList/{idList} - deleteTaskList
  // DELETE /api/taskList/tasks/{idList} - deleteTasksFromList
  // POST /api/taskList/tasks/move - moveTasksForList {idOldList, idNewList, List<Integer> tasks}

  // POST /api/task/ - createTask {taskName, taskDescription, deadline, idList}
  // PUT /api/task/{id} - updateTask {taskName, taskDescription, deadline, idList | null}
  // DELETE /api/task/{id} - deleteTask 
  // DELETE /api/task/ - deleteTasks {List<Integer> idsTasks}
  // PUT /api/task/{id}/complete - completeTask
  // PUT /api/task/{id}/uncomplete - uncompleteTask

}
