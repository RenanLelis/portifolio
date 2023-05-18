import { Request, Response } from "express"
import { Error, AppErrorType } from "../business/errorType";
import msg from '../business/messages/messages';
import token from '../business/token/token'
import taskListService from "../business/taskListService";
import taskService from "../business/taskService";

const getTasksAndLists = (req: Request, res: Response) => {
    const userID = token.getUserID(req);
    taskListService.getTasksAndLists(userID)
        .then(taskListsDTO => res.status(200).json(taskListsDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const getLists = (req: Request, res: Response) => {
    const userID = token.getUserID(req);
    taskListService.getLists(userID)
        .then(taskListsDTO => res.status(200).json(taskListsDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const getTasksByList = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const listID = req.params.id;
    const userID = token.getUserID(req);
    taskService.getTasksByList(+listID, userID)
        .then(tasks => res.status(200).json(tasks))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const createTaskList = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    taskListService.createTaskList(req.body.listName, req.body.listDescription, userID)
        .then(taskListDTO => res.status(200).json(taskListDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const updateTaskList = (req: Request, res: Response) => {
    if (!req.body || !req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const listID = +req.params.id;
    taskListService.updateTaskList(req.body.listName, req.body.listDescription, listID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const deleteTaskList = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const listID = +req.params.id;
    taskListService.deleteTaskList(listID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const completeTasksFromList = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const listID = +req.params.id;
    taskListService.completeTasksFromList(listID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const uncompleteTasksFromList = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const listID = +req.params.id;
    taskListService.uncompleteTasksFromList(listID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const moveTasksFromList = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    taskListService.moveTasksFromList(req.body.listIdOrigin, req.body.listIdDestiny, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const createTask = (req: Request, res: Response) => {
    if (!req.body) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    taskService.createTask(req.body.taskName, req.body.taskDescription, req.body.deadline, req.body.listId, userID)
        .then(taskDTO => res.status(200).json(taskDTO))
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const updateTask = (req: Request, res: Response) => {
    if (!req.body || !req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const taskID = +req.params.id;
    taskService.updateTask(req.body.taskName, req.body.taskDescription, req.body.deadline, taskID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const deleteTask = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const taskID = +req.params.id;
    taskService.deleteTask(taskID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const completeTask = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const taskID = +req.params.id;
    taskService.completeTask(taskID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const uncompleteTask = (req: Request, res: Response) => {
    if (!req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const taskID = +req.params.id;
    taskService.uncompleteTask(taskID, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

const moveTaskToList = (req: Request, res: Response) => {
    if (!req.body || !req.params || !req.params.id || isNaN(+req.params.id)) {
        return res.status(AppErrorType.INVALID_INPUT).json({
            errorMessage: msg.getErrorMessageInputValues()
        });
    }
    const userID = token.getUserID(req);
    const taskID = +req.params.id;
    taskService.moveTaskToList(taskID, req.body.listId, userID)
        .then(() => res.status(200).send())
        .catch(err => {
            if (err instanceof Error) return res.status(err.errorType).json({ errorMessage: err.errorMessage })
            else return res.status(AppErrorType.INTERN_ERROR).json({ errorMessage: msg.getErrorMessage() })
        })
}

export default {
    getTasksAndLists,
    getLists,
    getTasksByList,
    createTaskList,
    updateTaskList,
    deleteTaskList,
    completeTasksFromList,
    uncompleteTasksFromList,
    moveTasksFromList,
    createTask,
    updateTask,
    deleteTask,
    completeTask,
    uncompleteTask,
    moveTaskToList,
}