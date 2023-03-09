import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { STATUS_COMPLETE, STATUS_INCOMPLETE, Task } from '../model/task';
import { TaskList } from '../model/taskList';
import { AuthService } from './auth.service';
import { BASE_URL } from './consts';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  URL_TASK_LIST = BASE_URL + "/api/taskList/";
  URL_TASK_NO_LIST = BASE_URL + "/api/taskList/nolist/";
  URL_TASK_LISTS = BASE_URL + "/api/taskList/lists/";
  URL_TASK_LISTS_ONLY_LISTS = BASE_URL + "/api/taskList/tasks/";
  URL_TASK_LISTS_MOVE = BASE_URL + "/api/taskList/tasks/move/";

  URL_TASK = BASE_URL + "/api/task/";
  URL_TASK_COMPLETE = BASE_URL + "/api/task/complete/";
  URL_TASK_UNCOMPLETE = BASE_URL + "/api/task/uncomplete/";

  taskLists = new BehaviorSubject<TaskList[]>([]);

  constructor(private http: HttpClient) { }

  fetchTasksAndLists() {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.get(this.URL_TASK_LIST, { headers })
      .pipe(tap(resData => {
        console.log(resData);
        this.taskLists.next(resData as TaskList[]);
      }));
  }

  createTaskList(listName: string, listDescription: string) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.post(this.URL_TASK_LIST, { listName: listName, listDescription: listDescription }, { headers })
      .pipe(tap(resData => {
        console.log(resData);
        if (this.taskLists.value === null) { this.taskLists.next([]); }
        this.taskLists.value.push(resData as TaskList);
        this.taskLists.next(this.taskLists.value);
      }));
  }

  updateTaskList(listName: string, listDescription: string, idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.put(this.URL_TASK_LIST.concat(`${idList}`), { listName: listName, listDescription: listDescription }, { headers })
      .pipe(tap(resData => {
        console.log(resData);
        if (this.taskLists != null && this.taskLists.value != null) {
          this.taskLists.value.forEach(taskList => {
            if (taskList.id === idList) {
              taskList.listName = listName;
              taskList.listName = listDescription;
              this.taskLists.next(this.taskLists.value);
              return;
            }
          });
        }
      }));
  }

  deleteTaskList(idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.delete(this.URL_TASK_LIST.concat(`${idList}`), { headers })
      .pipe(tap(resData => {
        console.log(resData);
        if (this.taskLists != null && this.taskLists.value != null) {
          this.taskLists.next(this.taskLists.value.filter((value: TaskList) => {
            return value.id !== idList
          }));
        }
      }));
  }

  deleteTasksFromList(idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.delete(this.URL_TASK_LIST.concat(`${idList}`), { headers })
      .pipe(tap(resData => {
        console.log(resData);
        if (this.taskLists != null && this.taskLists.value != null) {
          this.taskLists.value.forEach((taskList) => {
            if (taskList.id === idList) {
              taskList.tasks = []
              this.taskLists.next(this.taskLists.value);
              return;
            }
          })
        }
      }));
  }

  moveTasksForList(idOldList: Number, idNewList: Number, tasksIds: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.post(this.URL_TASK_LISTS_MOVE, { idOldList: idOldList, idNewList: idNewList, tasks: tasksIds }, { headers })
      .pipe(tap(resData => {
        this.fetchTasksAndLists().subscribe();
      }));
  }

  createTask(taskName: string, taskDescription: string, deadline: Date | null, idList: Number | null) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.post(this.URL_TASK, { taskName: taskName, taskDescription: taskDescription, deadline: deadline, idList: idList }, { headers })
      .pipe(tap(resData => {
        let task: Task = resData as Task;
        for (let i = 0; i < this.taskLists.value.length; i++) {
          if (this.taskLists.value[i].id === idList || ((idList === 0 || idList === null) && (this.taskLists.value[i].id === 0 || this.taskLists.value[i].id === null))) {
            if (this.taskLists.value[i].tasks === null) {
              this.taskLists.value[i].tasks = [];
            }
            this.taskLists.value[i].tasks!.push(task);
            this.taskLists.next(this.taskLists.value);
            break;
          }
        }
      }));
  }

  updateTask(id: Number, taskName: string, taskDescription: string, deadline: Date | null) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.put(this.URL_TASK.concat(`${id}`), { taskName: taskName, taskDescription: taskDescription, deadline: deadline }, { headers })
      .pipe(tap(resData => {
        if (this.taskLists !== null && this.taskLists.value !== null) {
          this.taskLists.value.forEach((taskList) => {
            if (taskList.tasks !== null && taskList.tasks.length > 0) {
              taskList.tasks.forEach((task) => {
                if (task.id === id) {
                  task.taskName = taskName;
                  task.taskDescription = taskDescription;
                  task.deadline = deadline;
                  return;
                }
              });
            }
          })
        }
      }));
  }

  deleteTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.delete(this.URL_TASK.concat(`${id}`), { headers })
      .pipe(tap(resData => {
        this.taskLists.value.forEach(taskList => {
          if (taskList.tasks != null) {
            taskList.tasks = taskList.tasks.filter((task) => {
              return task.id !== id;
            })
          }
        })
      }))
  }

  deleteTasks(idsTasks: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    const options = { headers: headers, body: { idsTasks: idsTasks }, };
    return this.http.delete(this.URL_TASK, options)
      .pipe(tap(resData => {
        this.taskLists.value.forEach(taskList => {
          if (taskList.tasks != null) {
            taskList.tasks = taskList.tasks.filter((task) => {
              return !idsTasks.includes(task.id);
            })
          }
        })
      }))
  }

  completeTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.put(this.URL_TASK_COMPLETE.concat(`${id}`), null, { headers })
      .pipe(tap(resData => {
        this.taskLists.value.forEach(taskList => {
          if (taskList.tasks != null) {
            taskList.tasks.forEach(task => {
              if (task.id === id) {
                task.taskStatus = STATUS_COMPLETE;
                this.taskLists.next(this.taskLists.value);
                return;
              }
            });
          }
        })
      }));
  }

  uncompleteTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({ "Content-Type": "application/json", });
    return this.http.put(this.URL_TASK_UNCOMPLETE.concat(`${id}`), null, { headers })
      .pipe(tap(resData => {
        this.taskLists.value.forEach(taskList => {
          if (taskList.tasks != null) {
            taskList.tasks.forEach(task => {
              if (task.id === id) {
                task.taskStatus = STATUS_INCOMPLETE;
                this.taskLists.next(this.taskLists.value);
                return;
              }
            });
          }
        })
      }));
  }

}
