import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, tap } from 'rxjs';
import { TaskList, createDefaultTaskList } from '../model/taskList';
import { BASE_URL } from './consts';
import { STATUS_COMPLETE, STATUS_INCOMPLETE, Task } from '../model/task';

@Injectable({
  providedIn: 'root',
})
export class TaskService {
  URL_TASK_LIST = BASE_URL + '/api/taskList';
  URL_TASK_LISTS_MOVE = BASE_URL + '/api/taskList/tasks/move';
  URL_TASK_LISTS_MOVE_FROM_LIST = BASE_URL + '/api/taskList/tasks/moveFromList';
  URL_TASK_LISTS_COMPLETE_TASKS = BASE_URL + '/api/taskList/tasks/complete';
  URL_TASK_LISTS_UNCOMPLETE_TASKS = BASE_URL + '/api/taskList/tasks/uncomplete';
  URL_TASKS_BY_LISTS = BASE_URL + '/api/taskList/tasks';
  URL_TASK = BASE_URL + '/api/task';
  URL_TASK_MOVE = BASE_URL + '/api/task/list';
  URL_TASK_COMPLETE = BASE_URL + '/api/task/complete';
  URL_TASK_UNCOMPLETE = BASE_URL + '/api/task/uncomplete';
  URL_TASKS_DELETE = BASE_URL + '/api/tasks';
  URL_TASKS_COMPLETE = BASE_URL + '/api/tasks/complete';
  URL_TASKS_UNCOMPLETE = BASE_URL + '/api/tasks/uncomplete';

  taskLists = new BehaviorSubject<TaskList[]>([]);
  selectedTaskList: TaskList | null = null;
  showAllTasks: boolean = false;

  constructor(private http: HttpClient) { }

  fetchTasksAndLists() {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.get(this.URL_TASK_LIST, { headers }).pipe(
      tap((resData) => {
        this.updateSelectedListInfo();
        this.taskLists.next(resData as TaskList[]);
      })
    );
  }

  updateSelectedListInfo() {
    if (this.selectedTaskList !== null && this.selectedTaskList.id !== null && this.selectedTaskList.id > 0) {
      this.taskLists.value.forEach(taskList => {
        if ((taskList.id !== null && taskList.id.toString() === this.selectedTaskList!.id!.toString())) {
          this.selectedTaskList!.listName = taskList.listName;
          this.selectedTaskList!.listDescription = taskList.listDescription;
          return;
        }
      });
      //selected list is not null and is not on the taskLists, propably was deleted
      this.selectedTaskList = createDefaultTaskList();
    } else {
      this.selectedTaskList = createDefaultTaskList();
    }
  }

  fetchTasksByList(idList: number | null) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    let url = this.URL_TASKS_BY_LISTS.concat((idList !== null && idList! > 0) ? `/${idList}` : '');
    return this.http
      .get(url, { headers })
      .pipe(
        tap((resData) => {
          for (let i = 0; i < this.taskLists.value.length; i++) {
            if (this.taskLists.value[i].id === idList) {
              this.taskLists.value[i].tasks = resData as Task[];
              break;
            }
          }
        })
      );
  }

  getTasksByList(idList: number | null) {
    if (this.taskLists === null || this.taskLists.value === null || this.taskLists.value.length <= 0) {
      return [];
    }
    let result: Task[] = [];
    for (let i = 0; i < this.taskLists.value.length; i++) {
      const taskList = this.taskLists.value[i];
      if (((idList === null || idList! <= 0) && (taskList.id === null || taskList.id <= 0)) //default list
      || (taskList.id != null && idList != null && idList.toString() === taskList.id.toString())) {
        if (taskList.tasks !== null) {
          result.push(...taskList.tasks!);
        }
        return result;
      }
    }
    return result;
  }

  getAllTasks(): Task[] {
    if (this.taskLists === null || this.taskLists.value === null || this.taskLists.value.length <= 0) {
      return [];
    }
    let result: Task[] = [];
    this.taskLists.value.forEach(function (taskList) {
      if (taskList.tasks !== null) {
        result.push(...taskList.tasks!);
      }
    });
    return result;
  }

  createTaskList(listName: string, listDescription: string) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post(
      this.URL_TASK_LIST,
      { listName: listName, listDescription: listDescription },
      { headers }
    );
  }

  updateTaskList(listName: string, listDescription: string, idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LIST.concat(`/${idList}`),
      { listName: listName, listDescription: listDescription },
      { headers }
    ).pipe(tap((resData) => { this.updateSelectedListInfo(); }));
  }

  deleteTaskList(idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.delete(this.URL_TASK_LIST.concat(`/${idList}`), { headers }
    ).pipe(tap((resData) => { this.updateSelectedListInfo(); }));;
  }

  moveTasksForList(listId: Number, tasksIds: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_MOVE,
      { listId: listId, tasksIds: tasksIds },
      { headers }
    );
  }

  moveTasksFromList(listIdOrigin: number, listIdDestiny: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_MOVE_FROM_LIST,
      { listIdOrigin: listIdOrigin, listIdDestiny: listIdDestiny },
      { headers }
    );
  }

  completeTasksFromList(listId: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_COMPLETE_TASKS.concat(`/${listId}`),
      null,
      { headers }
    ).pipe(tap((resData) => {
      this.changeStatusTasksFromList(listId, STATUS_COMPLETE)
    }));
  }

  uncompleteTasksFromList(listId: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_UNCOMPLETE_TASKS.concat(`/${listId}`),
      null,
      { headers }
    ).pipe(tap((resData) => {
      this.changeStatusTasksFromList(listId, STATUS_INCOMPLETE)
    }));
  }

  changeStatusTasksFromList(listId: number, status: number) {
    if (this.taskLists.value != null && this.taskLists.value.length > 0) {
      for (let i = 0; i < this.taskLists.value.length; i++) {
        if (
          ((this.taskLists.value[i].id === null || this.taskLists.value[i].id! <= 0) && listId <= 0) // default list
          || (this.taskLists.value[i].id!.toString() === listId.toString()) // not default list
        ) {
          if (this.taskLists.value[i].tasks != null && this.taskLists.value[i].tasks!.length > 0) {
            this.taskLists.value[i].tasks!.forEach(task => { task.taskStatus = status });
            this.taskLists.next(this.taskLists.value);
          }
          return;
        }
      }
    }
  }

  createTask(taskName: string, taskDescription: string, deadline: string | null, listId: Number | null) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post(
      this.URL_TASK,
      {
        taskName: taskName,
        taskDescription: taskDescription,
        deadline: deadline,
        listId: listId,
      },
      { headers }
    );
  }

  updateTask(
    id: Number,
    taskName: string,
    taskDescription: string,
    deadline: string | null
  ) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK.concat(`/${id}`),
      {
        taskName: taskName,
        taskDescription: taskDescription,
        deadline: deadline,
      },
      { headers }
    );
  }

  moveTaskToList(id: number, listId: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_MOVE.concat(`/${id}`),
      { listId: listId },
      { headers }
    );
  }

  deleteTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.delete(this.URL_TASK.concat(`/${id}`), { headers });
  }

  deleteTasks(idsTasks: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.delete(this.URL_TASKS_DELETE, options);
  }

  completeTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(this.URL_TASK_COMPLETE.concat(`/${id}`), null, {
      headers,
    });
  }

  completeTasks(idsTasks: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.put(this.URL_TASKS_COMPLETE, options);
  }

  uncompleteTask(id: Number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(this.URL_TASK_UNCOMPLETE.concat(`/${id}`), null, {
      headers,
    });
  }

  uncompleteTasks(idsTasks: Number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.put(this.URL_TASKS_UNCOMPLETE, options);
  }

  getTaskListById(id: number | null): TaskList | null {
    for (let i = 0; i < this.taskLists.value.length; i++) {
      const taskList = this.taskLists.value[i];
      if ((taskList.id === null && id === null) || (taskList.id !== null && id != null && taskList.id!.toString() === id!.toString())) {
        return taskList;
      }
    }
    return null;
  }

  updateLocalTaskLists(listName: string, listDescription: string, id: number) {
    for (let i = 0; i < this.taskLists.value.length; i++) {
      if (this.taskLists.value[i].id === id) {
        // in this case the list was updated
        this.taskLists.value[i].listName = listName;
        this.taskLists.value[i].listDescription = listDescription;
        this.taskLists.next(this.taskLists.value);
        return;
      }
    }
    // in this case the list is new, the array of taskLists does not contains it
    const newTaskList: TaskList = {
      id: id,
      listName: listName,
      listDescription: listDescription,
      tasks: [],
    };
    let tempTasks = this.taskLists.value;
    tempTasks.push(newTaskList);
    this.taskLists.next(tempTasks);
  }
}
