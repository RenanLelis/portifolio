import { TaskList } from "../model/taskList"
import { STATUS_COMPLETE, STATUS_INCOMPLETE } from "./consts"
import { TaskListDTO, convertTaskListToDTO, convertTaskListsToDTOs, convertTasksAndListsToDTO } from "./dto/dto"
import { Error, AppErrorType } from "./errorType";
import msg from './messages/messages'
import taskListRepository from "../persistence/taskListRepository";
import taskRepository from "../persistence/taskRepository";

const getTasksAndLists = (userID: number | null): Promise<TaskListDTO[]> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));

        taskListRepository.getListsByUser(userID)
            .then(lists => {
                taskRepository.getTasksByUser(userID)
                    .then(tasks => { return resolve(convertTasksAndListsToDTO(lists, tasks)) })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const getLists = (userID: number | null): Promise<TaskListDTO[]> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));

        taskListRepository.getListsByUser(userID)
            .then(lists => { return resolve(convertTaskListsToDTOs(lists)) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const createTaskList = (listName: string | null, listDescription: string | null, userID: number | null): Promise<TaskListDTO> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listName === null || listName.trim().length <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        const description = listDescription !== null ? listDescription.trim() : "";
        taskListRepository.createNewList(listName.trim(), description, userID)
            .then(newID => { return resolve(convertTaskListToDTO(new TaskList(newID, listName, description, userID))) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const updateTaskList = (
    listName: string | null,
    listDescription: string | null,
    listID: number | null,
    userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listName === null || listName.trim().length <= 0 || listID === null || listID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        const description = listDescription !== null ? listDescription.trim() : "";
        taskListRepository.updateList(listID, userID, listName.trim(), description)
            .then(newID => { return resolve(null) })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const deleteTaskList = (listID: number | null, userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listID === null || listID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        taskRepository.deleteTasksFromList(listID, userID)
            .then(() => {
                taskListRepository.deleteList(listID, userID)
                    .then(() => { return resolve(null); })
                    .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
            })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const completeTasksFromList = (listID: number | null, userID: number | null): Promise<null> => {
    return changeStatusTasksFromList(listID, userID, STATUS_COMPLETE);
}

const uncompleteTasksFromList = (listID: number | null, userID: number | null): Promise<null> => {
    return changeStatusTasksFromList(listID, userID, STATUS_INCOMPLETE);
}

const changeStatusTasksFromList = (listID: number | null, userID: number | null, status: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listID === null || listID <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));

        taskListRepository.updateStatusTasksFromList(listID, userID, status)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const moveTasksFromList = (listIDOrigin: number | null, listIDDestiny: number | null, userID: number | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        if (userID === null || userID <= 0)
            return reject(new Error(AppErrorType.NOT_ALLOWED, msg.getErrorMessageToken()));
        if (listIDOrigin === null || listIDOrigin <= 0 || listIDDestiny === null || listIDDestiny <= 0)
            return reject(new Error(AppErrorType.INVALID_INPUT, msg.getErrorMessageInputValues()));
        taskListRepository.moveTasksFromList(listIDOrigin, listIDDestiny, userID)
            .then(() => { return resolve(null); })
            .catch(err => { return reject(new Error(AppErrorType.INTERN_ERROR, msg.getErrorMessage())) })
    });
}

const createDefaultList = (userID: number): TaskList => {
    return new TaskList(
        null,
        "My Tasks",
        "Default list for the user, created by the app",
        userID);
}

export default {
    getTasksAndLists,
    getLists,
    createTaskList,
    updateTaskList,
    deleteTaskList,
    completeTasksFromList,
    uncompleteTasksFromList,
    moveTasksFromList,
    createDefaultList
}