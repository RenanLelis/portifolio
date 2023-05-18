import { Task } from "../model/task"
import { MysqlError } from 'mysql';
import sql from './db'
import { STATUS_INCOMPLETE } from "../business/consts";

const getTasksByUser = (userID: number): Promise<Task[]> => {
    return new Promise((resolve, reject) => {
        sql.query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS,
         ID_LIST FROM TASK WHERE ID_USER = ? ORDER BY ID_LIST`, [userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                if (results !== null && results.length > 0) {
                    const tasks: Task[] = [];
                    results.forEach((row: {
                        ID: number | null | undefined;
                        TASK_NAME: string | undefined;
                        TASK_DESCRIPTION: string | null | undefined;
                        DEADLINE: string | null | undefined;
                        TASK_STATUS: number | undefined;
                        ID_LIST: number | undefined;
                    }) => {
                        const task = new Task(
                            row.ID,
                            row.TASK_NAME,
                            row.TASK_DESCRIPTION,
                            row.DEADLINE,
                            row.TASK_STATUS,
                            userID,
                            row.ID_LIST
                        )
                        tasks.push(task)
                    });
                    return resolve(tasks);
                }
                return resolve([]);
            });
    });
}

const getTasksByList = (listID: number, userID: number): Promise<Task[]> => {
    return new Promise((resolve, reject) => {
        sql.query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS FROM TASK WHERE ID_USER = ? AND ID_LIST = ?`,
            [userID, listID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                if (results !== null && results.length > 0) {
                    const tasks: Task[] = [];
                    results.forEach((row: {
                        ID: number | null | undefined;
                        TASK_NAME: string | undefined;
                        TASK_DESCRIPTION: string | null | undefined;
                        DEADLINE: string | null | undefined;
                        TASK_STATUS: number | undefined;
                    }) => {
                        const task = new Task(
                            row.ID,
                            row.TASK_NAME,
                            row.TASK_DESCRIPTION,
                            row.DEADLINE,
                            row.TASK_STATUS,
                            userID,
                            listID
                        )
                        tasks.push(task)
                    });
                    return resolve(tasks);
                }
                return resolve([]);
            });
    });
}

const deleteTasksFromList = (listID: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`DELETE FROM TASK WHERE ID_LIST = ? AND ID_USER = ?`,
            [listID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const createTask = (taskName: string, taskDescription: string | null, deadline: string | null, listID: number, userID: number): Promise<number> => {
    return new Promise((resolve, reject) => {
        sql.query(`INSERT INTO TASK (TASK_NAME, TASK_DESCRIPTION, 
            DEADLINE, ID_LIST, ID_USER, TASK_STATUS) VALUES (?, ?, ?, ?, ?, ?)`,
            [taskName, taskDescription, deadline, listID, userID, STATUS_INCOMPLETE],
            (err, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(results.insertId);
            });
    });
}

const updateTask = (taskName: string, taskDescription: string | null, deadline: string | null, taskID: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK SET TASK_NAME = ?, TASK_DESCRIPTION = ?, DEADLINE = ? WHERE ID = ? AND ID_USER = ?`,
            [taskName, taskDescription, deadline, taskID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const moveTaskToList = (taskID: number, listIDDestiny: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK SET ID_LIST = ? WHERE ID = ? AND ID_USER = ?`,
            [listIDDestiny, taskID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const deleteTask = (taskID: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`DELETE FROM TASK WHERE ID = ? AND ID_USER = ?`,
            [taskID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const updateStatusTask = (taskID: number, userID: number, status: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK SET TASK_STATUS = ? WHERE ID = ? AND ID_USER = ?`,
            [status, taskID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}


export default {
    getTasksByUser,
    getTasksByList,
    deleteTasksFromList,
    createTask,
    updateTask,
    moveTaskToList,
    deleteTask,
    updateStatusTask
}