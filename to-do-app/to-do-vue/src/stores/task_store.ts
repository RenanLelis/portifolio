import { STATUS_COMPLETE, STATUS_INCOMPLETE, type Task } from "@/model/task";
import type { TaskList } from "@/model/task_list"
import { defineStore } from "pinia"
import { ref, type Ref } from "vue"
import { sendHttpRequest } from "./interceptor";
import { URL_TASK_UNCOMPLETE, URL_TASK_COMPLETE, URL_TASK, URL_TASK_LIST, URL_TASK_LISTS_COMPLETE_TASKS, URL_TASK_LISTS_MOVE_FROM_LIST, URL_TASK_LISTS_UNCOMPLETE_TASKS, URL_TASK_MOVE, URL_TASKS_BY_LISTS } from "@/consts/consts";

export const useTaskStore = defineStore('task', () => {

    const selectedList: Ref<TaskList | null> = ref(null);
    const lists: Ref<TaskList[]> = ref([]);
    const isSetShowAllTasks = ref(false);
    const tasks: Ref<Task[]> = ref([]);
    let isAppInitialized = false;

    const fetchTasksAndLists = () => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LIST, 'GET', "")
                .then(res => {
                    res.json()
                        .then(body => {
                            if (body.errorMessage) return reject(body);
                            updateTasksAndLists(body as TaskList[])
                            return resolve(null)
                        })
                        .catch(error => { console.log(error); return reject(error); })
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const updateTasksAndLists = (newLists: TaskList[]) => {
        lists.value = newLists;
        if (newLists.length > 0) {
            if (!isSetShowAllTasks.value && selectedList.value !== null) {
                for (let i = 0; i < newLists.length; i++) {
                    if (newLists[i].id === selectedList.value.id) {
                        selectedList.value = newLists[i];
                        tasks.value = newLists[i].tasks
                        break;
                    }
                }
            } else if (!isSetShowAllTasks.value) {
                selectedList.value = newLists[0];
                tasks.value = newLists[0].tasks;
            } else {
                tasks.value = []
                newLists.forEach(list => {
                    tasks.value.push(...list.tasks);
                })
            }
        }
    }

    const fetchTasksByList = (idList: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASKS_BY_LISTS + `/${idList}`, 'GET', "")
                .then(res => {
                    res.json()
                        .then(body => {
                            if (body.errorMessage) return reject(body);
                            updateTasksOnList(idList, body as Task[])
                            return resolve(null)
                        })
                        .catch(error => { console.log(error); return reject(error); })
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const updateTasksOnList = (idList: number, tasks: Task[]) => {
        for (let i = 0; i < lists.value.length; i++) {
            if (lists.value[i].id === idList) {
                lists.value[i].tasks = tasks
                break;
            }
        }
        if (selectedList.value !== null && selectedList.value!.id === idList) {
            selectedList.value.tasks = tasks;
        }
    }

    const createTaskList = (listName: string, listDescription: string) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LIST, 'POST', JSON.stringify({
                "listName": listName, "listDescription": listDescription
            }))
                .then(res => {
                    res.json()
                        .then(body => {
                            if (body.errorMessage) return reject(body);
                            const taskList = body as TaskList;
                            lists.value.push(taskList);
                            selectTaskList(taskList);
                            return resolve(null);
                        })
                        .catch(error => { console.log(error); return reject(error); })
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const selectTaskList = (taskList: TaskList) => {
        if (taskList !== null) {
            isSetShowAllTasks.value = false;
            selectedList.value = taskList;
            tasks.value = taskList.tasks;
        } else {
            selectedList.value = null;
        }
    }

    const updateTaskList = (listName: string, listDescription: string, idList: Number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LIST + `/${idList}`, 'PUT', JSON.stringify({
                "listName": listName, "listDescription": listDescription
            }))
                .then(res => {
                    if (res.ok) {
                        updateLocalList(listName, listDescription, idList)
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const updateLocalList = (listName: string, listDescription: string, idList: Number) => {
        for (let i = 0; i < lists.value.length; i++) {
            if (lists.value[i].id === idList) {
                lists.value[i].listName = listName;
                lists.value[i].listDescription = listDescription;
                return;
            }
        }
    }

    const deleteTaskList = (idList: Number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LIST + `/${idList}`, 'DELETE', "")
                .then(res => {
                    if (res.ok) {
                        removeLocalList(idList);
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const removeLocalList = (idList: Number) => {
        lists.value = lists.value.filter(taskList => taskList.id !== idList)
        if (selectedList.value !== null && selectedList.value.id === idList && lists.value.length > 0) {
            selectTaskList(lists.value[0]);
        }
    }

    const moveTasksFromList = (listIdOrigin: number, listIdDestiny: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LISTS_MOVE_FROM_LIST, "PUT", JSON.stringify({
                'listIdOrigin': listIdOrigin, 'listIdDestiny': listIdDestiny
            }))
                .then(res => {
                    if (res.ok) {
                        let indexOrigin: number | null = null;
                        let indexDestiny: number | null = null;
                        const templists = lists.value;
                        for (let i = 0; i < templists.length; i++) {
                            if (templists[i].id!.toString() === listIdOrigin.toString()) { indexOrigin = i; }
                            if (templists[i].id!.toString() === listIdDestiny.toString()) { indexDestiny = i; }
                            if (indexOrigin !== null && indexDestiny !== null) { break; }
                        }
                        if (indexOrigin !== null && indexDestiny !== null) {
                            const tasks = templists[indexOrigin].tasks
                            templists[indexDestiny].tasks = tasks
                            templists[indexOrigin].tasks = []
                            lists.value = templists;
                        }
                        tasks.value = [];
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const completeTasksFromList = (listId: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LISTS_COMPLETE_TASKS.concat(`/${listId}`), 'PUT', "")
                .then(res => {
                    if (res.ok) {
                        updateStatusTasksFromList(listId, STATUS_COMPLETE);
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const uncompleteTasksFromList = (listId: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_LISTS_UNCOMPLETE_TASKS.concat(`/${listId}`), 'PUT', "")
                .then(res => {
                    if (res.ok) {
                        updateStatusTasksFromList(listId, STATUS_INCOMPLETE);
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        })
    }

    const updateStatusTasksFromList = (listId: number, status: number) => {
        for (let i = 0; i < lists.value.length; i++) {
            if (lists.value[i].id === listId) {
                lists.value[i].tasks.forEach(task => {
                    task.taskStatus = status;
                })
                selectTaskList(lists.value[i]);
                tasks.value.forEach(task => {
                    task.taskStatus = status;
                });
                return;
            }
        }
    }

    const createTask = (taskName: string, taskDescription: string, deadline: string | null, listId: Number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK, 'POST', JSON.stringify({
                'taskName': taskName, 'taskDescription': taskDescription, 'deadline': deadline, 'listId': listId
            }))
                .then(res => {
                    res.json()
                        .then(body => {
                            if (body.errorMessage) return reject(body);
                            const newTask = body as Task;
                            tasks.value.push(newTask);
                            for (let i = 0; i < lists.value.length; i++) {
                                if (lists.value[i].id === listId) {
                                    lists.value[i].tasks.push(newTask);
                                    return resolve(null);
                                }
                            }
                            return resolve(null);
                        })
                        .catch(error => { console.log(error); return reject(error); })
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const updateTask = (id: number, taskName: string, taskDescription: string, deadline: string | null) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK.concat(`/${id}`), 'PUT', JSON.stringify({
                'taskName': taskName, 'taskDescription': taskDescription, 'deadline': deadline
            }))
                .then(res => {
                    if (res.ok) {
                        for (let i = 0; i < tasks.value.length; i++) {
                            if (tasks.value[i].id === id) {
                                tasks.value[i].taskName = taskName;
                                tasks.value[i].deadline = deadline;
                                tasks.value[i].taskDescription = taskDescription;
                                break;
                            }
                        }
                        for (let i = 0; i < lists.value.length; i++) {
                            for (let j = 0; j < tasks.value.length; j++) {
                                if (lists.value[i].tasks[j].id === id) {
                                    lists.value[i].tasks[j].taskName = taskName;
                                    lists.value[i].tasks[j].deadline = deadline;
                                    lists.value[i].tasks[j].taskDescription = taskDescription;
                                    return resolve(null);
                                }
                            }
                        }
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const moveTaskToList = (id: number, listId: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_MOVE.concat(`/${id}`), 'PUT', JSON.stringify({
                'listId': listId
            }))
                .then(res => {
                    if (res.ok) {
                        tasks.value = tasks.value.filter(task => task.id !== id);
                        let listDestinyIndex = -1;
                        let taskMoved: Task | null = null;
                        for (let i = 0; i < lists.value.length; i++) {
                            if (lists.value[i].id === listId) {
                                listDestinyIndex = i;
                            }
                            if (taskMoved === null) {
                                lists.value[i].tasks.forEach(task => {
                                    if (task.id === id) {
                                        taskMoved = task;
                                    }
                                });
                            }
                            lists.value[i].tasks = lists.value[i].tasks.filter(task => task.id !== id);
                        }
                        if (taskMoved !== null) {
                            lists.value[listDestinyIndex].tasks.push(taskMoved);
                        }
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const deleteTask = (id: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK.concat(`/${id}`), 'DELETE', "")
                .then(res => {
                    if (res.ok) {
                        tasks.value = tasks.value.filter(task => task.id !== id)
                        lists.value.forEach(list => {
                            list.tasks = list.tasks.filter(task => task.id !== id)
                        })
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const completeTask = (id: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_COMPLETE.concat(`/${id}`), 'PUT', "")
                .then(res => {
                    if (res.ok) {
                        updateStatusTask(id, STATUS_COMPLETE);
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const uncompleteTask = (id: number) => {
        return new Promise((resolve, reject) => {
            sendHttpRequest(URL_TASK_UNCOMPLETE.concat(`/${id}`), 'PUT', "")
                .then(res => {
                    if (res.ok) {
                        updateStatusTask(id, STATUS_INCOMPLETE);
                        return resolve(null);
                    } else {
                        res.json()
                            .then(body => { return reject(body); })
                            .catch(error => { console.log(error); return reject(error); })
                    }
                })
                .catch(error => { console.log(error); return reject(error); })
        });
    }

    const updateStatusTask = (id: number, newStatus: number) => {
        for (let i = 0; i < tasks.value.length; i++) {
            if (tasks.value[i].id === id) {
                tasks.value[i].taskStatus = newStatus;
                break;
            }
        }
        for (let i = 0; i < lists.value.length; i++) {
            for (let j = 0; j < lists.value[i].tasks.length; j++) {
                if (lists.value[i].tasks[j].id === id) {
                    lists.value[i].tasks[j].taskStatus = newStatus;
                    return;
                }
            }
        }
    }

    const setShowAllTasks = () => {
        isSetShowAllTasks.value = true;
        selectedList.value = null;
        tasks.value = getAllTasks();
    }

    const getAllTasks = (): Task[] => {
        const tasks: Task[] = []
        lists.value.forEach(list => {
            tasks.push(...list.tasks);
        })
        return tasks;
    }

    const getListById = (id: number): TaskList | null => {
        for (let i = 0; i < lists.value.length; i++) {
            if (lists.value[i].id === id) {
                return lists.value[i];
            }
        }
        return null;
    }

    const getTaskById = (id: number): Task | null => {
        for (let i = 0; i < lists.value.length; i++) {
            for (let j = 0; j < lists.value[i].tasks.length; j++) {
                if (lists.value[i].tasks[j].id === id) { return lists.value[i].tasks[j] }
            }
        }
        return null;
    }

    return {
        fetchTasksAndLists,
        fetchTasksByList,
        createTaskList,
        updateTaskList,
        deleteTaskList,
        moveTasksFromList,
        completeTasksFromList,
        uncompleteTasksFromList,
        createTask,
        updateTask,
        moveTaskToList,
        deleteTask,
        completeTask,
        uncompleteTask,
        selectTaskList,
        setShowAllTasks,
        getListById,
        getTaskById,
        selectedList,
        lists,
        isSetShowAllTasks,
        tasks,
        isAppInitialized
    }
})