import { TaskList } from "../model/taskList";
import { MysqlError } from 'mysql';
import sql from './db'

const getListsByUser = (userID: number): Promise<TaskList[]> => {
    return new Promise((resolve, reject) => {
        sql.query(`SELECT ID, LIST_NAME, LIST_DESCRIPTION FROM TASK_LIST WHERE ID_USER = ?`, [userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                if (results !== null && results.length > 0) {
                    const lists: TaskList[] = [];
                    results.forEach((row: {
                        ID: number | null | undefined;
                        LIST_NAME: string | undefined;
                        LIST_DESCRIPTION: string | null | undefined;
                    }) => {
                        const list = new TaskList(
                            row.ID,
                            row.LIST_NAME,
                            row.LIST_DESCRIPTION,
                            userID
                        )
                        lists.push(list)
                    });
                    return resolve(lists);
                }
                return resolve([]);
            });
    });
}

const createNewList = (listName: string, listDescription: string | null, userID: number): Promise<number> => {
    return new Promise((resolve, reject) => {
        sql.query(`INSERT INTO TASK_LIST (LIST_NAME, LIST_DESCRIPTION, ID_USER) VALUES (?, ?, ?)`,
            [listName, listDescription, userID],
            (err, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(results.insertId);
            });
    });
}

const updateList = (listID: number, userID: number, listName: string, listDescription: string | null): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK_LIST SET LIST_NAME = ?, LIST_DESCRIPTION = ? WHERE ID = ? AND ID_USER = ?`,
            [listName, listDescription, listID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const deleteList = (listID: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`DELETE FROM TASK_LIST WHERE ID = ? AND ID_USER = ?`,
            [listID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const updateStatusTasksFromList = (listID: number, userID: number, taskStatus: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK SET TASK_STATUS = ? WHERE ID_LIST = ? AND ID_USER = ?`,
            [taskStatus, listID, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}

const moveTasksFromList = (listIDOrigin: number, listIDDestiny: number, userID: number): Promise<null> => {
    return new Promise((resolve, reject) => {
        sql.query(`UPDATE TASK SET ID_LIST = ? WHERE ID_LIST = ? AND ID_USER = ?`,
            [listIDDestiny, listIDOrigin, userID],
            (err: MysqlError | null, results) => {
                if (err != null) { console.log("error: ", err); return reject(err); }
                return resolve(null);
            })
    });
}


export default {
    getListsByUser,
    createNewList,
    updateList,
    deleteList,
    updateStatusTasksFromList,
    moveTasksFromList,
}