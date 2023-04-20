import { Injectable } from '@angular/core';
import { BASE_URL } from './consts';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TaskList, createDefaultTaskList } from '../model/taskList';
import { BehaviorSubject, tap } from 'rxjs';
import { STATUS_COMPLETE, STATUS_INCOMPLETE, Task } from '../model/task';

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

  selectTaskList(selectedTaskList: TaskList | null) {
    if (selectedTaskList !== null) {
      this.isSetShowAllTasks.next(false);
      this.selectedList.next(selectedTaskList);
      this.tasks.next(this.getTasksFromSelectedList());
    } else {
      this.selectedList.next(null);
    }
  }

  getList(id: number): TaskList {
    if (id <= 0) {
      return this.getDefaultList();
    }
    if (this.lists.value != null) {
      for (let i = 0; i < this.lists.value.length; i++) {
        const list = this.lists.value[i];
        if (list.id !== null && list.id!.toString() === id.toString()) {
          return list;
        }
      }
    }
    return this.getDefaultList();
  }

  getTask(id: number): Task | null {
    if (id <= 0) {
      return null;
    }
    if (this.lists.value != null) {
      for (let j = 0; j < this.lists.value.length; j++) {
        const list = this.lists.value[j];
        console.log(list);
        if (list.tasks !== null && list.tasks.length > 0) {
          for (let i = 0; i < list.tasks.length; i++) {
            const task = list.tasks[i];
            if (task.id.toString() === id.toString()) {
              return task
            }
          }
        }
      }
    }
    return null;
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
    if (this.selectedList.value === null) { return false; }
    if ((listId === null || listId! <= 0) && (this.selectedList.value.id === null || this.selectedList.value.id <= 0)) {
      return true; // selected list is default list
    }
    return (
      (listId !== null && listId! > 0) && (this.selectedList.value.id !== null && this.selectedList.value.id > 0)
      && listId.toString() === this.selectedList.value.id.toString())
  }

  fetchTasksAndLists() {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.get(this.URL_TASK_LIST, { headers: headers }).pipe(tap(resData => {
      let lists = resData as TaskList[];
      if (lists !== null && lists.length > 0) {
        for (let i = 0; i < lists.length; i++) {
          if (lists[i].id === null || lists[i].id! <= 0) {
            lists[i] = this.getDefaultList();
          }
        }
      }
      this.lists.next(lists)
    }));
  }

  getDefaultList(): TaskList {
    const lists = this.lists.value;
    if (lists !== null && lists.length > 0) { // lists is not empty
      for (let i = 0; i < lists.length; i++) {
        if (lists[i].id === null || lists[i].id! <= 0) {
          return lists[i];
        }
      }
    } else { // lists is empty
      const defaultList = createDefaultTaskList();
      lists.push(defaultList);
      this.lists.next(lists);
      return defaultList;
    }
    return createDefaultTaskList();
  }

  fetchTasksByList(idList: number | null) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    let url = this.URL_TASKS_BY_LISTS.concat((idList !== null && idList! > 0) ? `/${idList}` : '');
    return this.http.get(url, { headers }).pipe(tap(value => {
      this.tasks.next(value as Task[]);
    }));
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

  deleteTaskList(idList: number) {
    let headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.delete(this.URL_TASK_LIST.concat(`/${idList}`), { headers }).pipe(
      tap((value) => {
        this.selectTaskList(this.getDefaultList());
        this.lists.next(this.lists.value.filter(list => {
          return list.id === null || list.id!.toString() !== idList.toString()
        }));
      }));
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

  completeTasksFromList(listId: number | null) {
    if (listId === null) {
      listId = 0;
    }
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_COMPLETE_TASKS.concat(`/${listId}`),
      null,
      { headers }
    ).pipe(tap((value) => {
      this.updateStatusTasks(this.getTasksFromList(listId!), STATUS_COMPLETE)
    }));
  }

  uncompleteTasksFromList(listId: number | null) {
    if (listId === null) {
      listId = 0;
    }
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(
      this.URL_TASK_LISTS_UNCOMPLETE_TASKS.concat(`/${listId}`),
      null,
      { headers }
    ).pipe(tap((value) => {
      this.updateStatusTasks(this.getTasksFromList(listId!), STATUS_INCOMPLETE)
    }));
  }

  getTasksFromList(listId: number) {
    let tasks: number[] = []
    for (let i = 0; i < this.lists.value.length; i++) {
      const list = this.lists.value[i];
      if (((listId <= 0 && (list.id === null || list.id <= 0)) || listId.toString() === list.id!.toString()) && list.tasks !== null && list.tasks.length > 0) {
        list.tasks.forEach(task => {
          tasks.push(task.id);
        })
        break;
      }
    }
    return tasks;
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
    id: number,
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

  deleteTask(id: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.delete(this.URL_TASK.concat(`/${id}`), { headers });
  }

  deleteTasks(idsTasks: number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.delete(this.URL_TASKS_DELETE, options);
  }

  completeTask(id: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(this.URL_TASK_COMPLETE.concat(`/${id}`), null, { headers }).pipe(tap(value => {
      this.updateStatusTask(id, STATUS_COMPLETE)
    }));
  }

  completeTasks(idsTasks: number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.put(this.URL_TASKS_COMPLETE, options);
  }

  uncompleteTask(id: number) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.put(this.URL_TASK_UNCOMPLETE.concat(`/${id}`), null, {
      headers,
    }).pipe(tap(value => {
      this.updateStatusTask(id, STATUS_INCOMPLETE)
    }));
  }

  uncompleteTasks(idsTasks: number[]) {
    let headers: HttpHeaders = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    const options = { headers: headers, body: { idsTasks: idsTasks } };
    return this.http.put(this.URL_TASKS_UNCOMPLETE, options);
  }

  updateStatusTasks(tasksIDs: number[], status: number) {
    let tasks: Task[] = []
    tasks.push(...this.tasks.value)
    for (let i = 0; i < tasks.length; i++) {
      if (tasksIDs.indexOf(tasks[i].id) >= 0) {
        tasks[i].taskStatus = status;
      }
    }
    this.tasks.next(tasks);

    let lists: TaskList[] = [];
    lists.push(...this.lists.value);
    for (let i = 0; i < lists.length; i++) {
      if (lists[i].tasks !== null) {
        for (let j = 0; j < lists[i].tasks!.length; j++) {
          if (tasksIDs.indexOf(lists[i].tasks![j].id) >= 0) {
            lists[i].tasks![j].taskStatus = status;
          }
        }
      }
    }
    this.lists.next(lists);
  }

  updateStatusTask(taskID: number, status: number) {
    let tasks: Task[] = []
    tasks.push(...this.tasks.value)
    for (let i = 0; i < tasks.length; i++) {
      if (taskID.toString() === tasks[i].id.toString()) {
        tasks[i].taskStatus = status;
        this.tasks.next(tasks);
        break;
      }
    }
    let lists: TaskList[] = [];
    lists.push(...this.lists.value);
    for (let i = 0; i < lists.length; i++) {
      if (lists[i].tasks !== null) {
        for (let j = 0; j < lists[i].tasks!.length; j++) {
          if (lists[i].tasks![j].id.toString() === taskID.toString()) {
            lists[i].tasks![j].taskStatus = status;
            this.lists.next(lists);
            return;
          }
        }
      }
    }

  }

}
