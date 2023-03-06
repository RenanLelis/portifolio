const sql = require("./db.js");
const List = require("../model/list");
const Task = require("../model/task");

const getListsAndTasksByUser = (idUser, result) => {}

const getTasksByList = (idList, result) => {
    sql.query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS, ID_USER 
     FROM TASK WHERE AND ID_LIST = ?`, [idList], (err, res) => {
        if (err) {
            result(err, null);
            return;
        }
        if (res.length) {
            const tasks = []
            for (let i = 0; i < res.length; i++) {
                let task = new Task(
                    res[i].ID,
                    res[i].TASK_NAME,
                    res[i].TASK_DESCRIPTION,
                    res[i].DEADLINE,
                    res[i].TASK_STATUS,
                    res[i].USER_NAME,
                    idList,
                    res[i].ID_USER
                );
                tasks.push(task)
            }
            result(null, tasks);
            return;
        }
        result(null, null);
    });
}

const getListsByUser = (idUser, result) => {}

const createList = (listName, listDescription, idUser, result) => {
    sql.query(`INSERT INTO LIST (LIST_NAME, LIST_DESCRIPTION, ID_USER)
    		 VALUES (?, ?, ?)`,
        [listName, listDescription, idUser],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, { id: res.insertId });
        }
    );
}

const updateList = (idList, listName, listDescription, result) => {
    sql.query(`UPDATE LIST SET LIST_NAME = ?, LIST_DESCRIPTION = ? WHERE ID = ?`,
        [listName, listDescription, idList],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const deleteList = (idList, result) => {
    deleteTasksFromList(idList, (error, res) => {
        if (error !== null) { 
            result(error, null); 
        } else {
            sql.query(`DELETE FROM LIST WHERE ID = ?`,
                [idList],
                (err, res) => {
                    if (err) {
                        result(err, null);
                        return
                    }
                    result(null, res);
                }
            );
        }
    })
    
}

const deleteTasksFromList = (idList, result) => {
    sql.query(`DELETE FROM TASK WHERE ID_LIST = ?`,
        [idList],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const moveTasksForList = (idList, tasks /* array de ids das tasks */, result) => {
    sql.query(`UPDATE TASK SET ID_LIST = ? WHERE ID IN (?)`,
        [idList, tasks],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const getTasksUserWithoutList = (idUser, result) => {
    sql.query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS 
     FROM TASK WHERE ID_USER = ? AND ID_LIST = NULL`, [idUser], (err, res) => {
        if (err) {
            result(err, null);
            return;
        }
        if (res.length) {
            const tasks = []
            for (let i = 0; i < res.length; i++) {
                let task = new Task(
                    res[i].ID,
                    res[i].TASK_NAME,
                    res[i].TASK_DESCRIPTION,
                    res[i].DEADLINE,
                    res[i].TASK_STATUS,
                    res[i].USER_NAME,
                    null,
                    idUser
                );
                tasks.push(task)
            }
            result(null, tasks);
            return;
        }
        result(null, null);
    });
}

const createTask = (idUser, taskName, taskDescription, deadline, taskStatus, idList, result) => {
    sql.query(`INSERT INTO TASK (TASK_NAME, TASK_DESCRIPTION, , DEADLINE, TASK_STATUS, ID_USER, ID_LIST)
    		 VALUES (?, ?, ?, ?, ?, ?)`,
        [taskName, taskDescription, deadline, taskStatus, idUser, idList],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, { id: res.insertId });
        }
    );
}

const updateTask = (idTask, taskName, taskDescription, deadline, taskStatus, idList, result) => {
    sql.query(`UPDATE TASK SET TASK_NAME = ?, TASK_DESCRIPTION = ?, DEADLINE = ?, TASK_STATUS = ?, ID_LIST = ? WHERE ID = ?`,
        [taskName, taskDescription, deadline, taskStatus, idList, idTask],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const deleteTask = (idTask, result) => {
    sql.query(`DELETE FROM TASK WHERE ID = ?`,
        [idTask],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const completeTask = (idTask, result) => {
    changeStatusTask(idTask, Task.STATUS_COMPLETE, result)
}

const changeStatusTask = (idTask, status, result) => {
    sql.query(`UPDATE TASK SET TASK_STATUS = ? WHERE ID = ?`,
        [status, idTask],
        (err, res) => {
            if (err) {
                result(err, null);
                return
            }
            result(null, res);
        }
    );
}

const unCompleteTask = (idTask, result) => {
    changeStatusTask(idTask, Task.STATUS_INCOMPLETE, result)
}

module.exports = {
    getListsAndTasksByUser,
    getListsByUser,
    getTasksByList,
    createList,
    updateList,
    deleteList,
    deleteTasksFromList,
    moveTasksForList,
    getTasksUserWithoutList,
    createTask,
    updateTask,
    deleteTask,
    completeTask,
    unCompleteTask
}