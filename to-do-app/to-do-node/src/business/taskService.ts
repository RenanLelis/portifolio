import { Task } from "../model/task";
import taskRepository from "../persistence/taskRepository";
import { STATUS_COMPLETE, STATUS_INCOMPLETE } from "./consts";
import { TaskDTO, convertTaskToDTO, convertTasksToDTOs } from "./dto/dto";
import { Error, AppErrorType } from "./errorType";
import msg from "./messages/messages";

const getTasksByList = (listID: number | null, userID: number | null): Promise<TaskDTO[]> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listID === null || listID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.getTasksByList(listID, userID)
            .then(tasks => { return resolve(convertTasksToDTOs(tasks)) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const createTask = (
    taskName: string | null,
    taskDescription: string | null,
    deadline: string | null,
    listID: number | null,
    userID: number | null): Promise<TaskDTO> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listID === null || listID <= 0 || !isTaskDataValid(taskName, taskDescription, deadline))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.createTask(
            taskName!.trim(),
            taskDescription !== null ? taskDescription.trim() : "",
            deadline !== null ? deadline.trim() : "",
            listID, userID)
            .then(newTask => { return resolve(convertTaskToDTO(new Task(newTask, taskName!.trim(), taskDescription, deadline, STATUS_INCOMPLETE, userID, listID))) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const isTaskDataValid = (taskName: string | null, taskDescription: string | null, deadline: string | null): boolean => {
    return (taskName !== null && taskName.trim().length > 0 && isDeadlineValid(deadline));
}

const isDeadlineValid = (deadline: string | null): boolean => {
    if (deadline === null || deadline.trim().length <= 0) return true;
    const deadlineFormatted = deadline.trim();
    if (deadlineFormatted.length !== 10) return false;
    var regEx = /^\d{4}-\d{2}-\d{2}$/;
    if (!deadlineFormatted.match(regEx)) return false;  // Invalid format
    var d = new Date(deadlineFormatted);
    var dNum = d.getTime();
    if (!dNum && dNum !== 0) return false; // NaN value, Invalid date
    return d.toISOString().slice(0, 10) === deadlineFormatted;
}

const updateTask = (
    taskName: string | null,
    taskDescription: string | null,
    deadline: string | null,
    taskID: number | null,
    userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (taskID === null || taskID <= 0 || !isTaskDataValid(taskName, taskDescription, deadline))
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.updateTask(
            taskName!.trim(),
            taskDescription !== null ? taskDescription.trim() : "",
            deadline !== null ? deadline.trim() : "",
            taskID, userID)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const moveTaskToList = (taskID: number | null, listIDDestiny: number | null, userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (taskID === null || taskID <= 0 || listIDDestiny === null || listIDDestiny <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.moveTaskToList(taskID, listIDDestiny, userID)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const deleteTask = (taskID: number | null, userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (taskID === null || taskID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.deleteTask(taskID, userID)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const completeTask = (taskID: number | null, userID: number | null): Promise<null> => {
    return changeStatusTask(taskID, userID, STATUS_COMPLETE);
}

const uncompleteTask = (taskID: number | null, userID: number | null): Promise<null> => {
    return changeStatusTask(taskID, userID, STATUS_INCOMPLETE);
}

const changeStatusTask = (taskID: number | null, userID: number | null, status: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (taskID === null || taskID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskRepository.updateStatusTask(taskID, userID, status)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

export default {
    getTasksByList,
    createTask,
    updateTask,
    moveTaskToList,
    deleteTask,
    completeTask,
    uncompleteTask,
}