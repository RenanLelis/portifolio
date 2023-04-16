import { Injectable } from '@angular/core';
import { BASE_URL } from './consts';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TaskList } from '../model/taskList';
import { BehaviorSubject, tap } from 'rxjs';
import { Task } from '../model/task';

@Injectable({
  providedIn: 'root'
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

  constructor(private http: HttpClient) { }

  lists = new BehaviorSubject<TaskList[]>([]);
  selectedList = new BehaviorSubject<TaskList | null>(null);
  isSetShowAllTasks = new BehaviorSubject<boolean>(false);
  tasks = new BehaviorSubject<Task[]>([]);

  showAllTasks() {
    this.isSetShowAllTasks.next(true);
    this.selectedList.next(null);
    this.tasks.next(this.getAllTasks());
  }

  selectTaskList(selectedTaskList: TaskList) {
    this.isSetShowAllTasks.next(false);
    this.selectedList.next(selectedTaskList);
    this.tasks.next(this.getTasksFromSelectedList());
  }

  getAllTasks() {
    let tasks: Task[] = []
    this.lists.value.forEach(list => {
      if (list.tasks !== null && list.tasks.length > 0) { tasks.push(...list.tasks!) }
    });
    return tasks;
  }

  getTasksFromSelectedList() {
    let tasks: Task[] = []
    for (let i = 0; i < this.lists.value.length; i++) {
      const list = this.lists.value[i];
      if (this.isSelectedList(list.id) && list.tasks !== null && list.tasks.length > 0) {
        tasks.push(...list.tasks!)
        break;
      }
    }
    return tasks;
  }

  isSelectedList(listId: number | null) {
    return (this.selectedList.value !== null &&
      (((this.selectedList.value.id === null || this.selectedList.value.id <= 0) && (listId === null || listId <= 0)) ||
        (this.selectedList.value.id!.toString() === listId!.toString())));
  }

  fetchTasksAndLists() {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.get(this.URL_TASK_LIST, { headers });
  }

  fetchTasksByList(idList: number | null) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    let url = this.URL_TASKS_BY_LISTS.concat((idList !== null && idList! > 0) ? `/${idList}` : '');
    return this.http.get(url, { headers });
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
    );
  }

  deleteTaskList(idList: Number) {
    let headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.delete(this.URL_TASK_LIST.concat(`/${idList}`), { headers }
    );
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
    );
  }

  uncompleteTasksFromList(listId: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_UNCOMPLETE_TASKS.concat(`/${listId}`),
      null,
      { headers }
    );
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
    ).pipe(tap(value => {
      console.log(value);
      // TODO check selected list for null, showAllTasks - set to defaultList
      // this.selectedList.value!.tasks!.push(value as Task)
    }));
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
    return this.http.put(this.URL_TASK_COMPLETE.concat(`/${id}`), null, { headers });
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

}
