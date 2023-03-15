package repository

import (
	"database/sql"
	"errors"
	"fmt"
	"strings"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/model"
)

// TaskListRepository represents a task and list repository for database connection
type TaskListRepository struct {
	db *sql.DB
}

// CreateTaskListRepository create a new task list repository
func CreateTaskListRepository(db *sql.DB) *TaskListRepository {
	return &TaskListRepository{db}
}

// GetTaskListsByUser fetch the lists on the database by user id
func (repo TaskListRepository) GetTaskListsByUser(userID uint64) ([]model.TaskList, error) {
	queryResult, err := repo.db.Query(`SELECT ID, LIST_NAME, LIST_DESCRIPTION 
	FROM LIST WHERE ID_USER = ?`, userID)
	if err != nil {
		return nil, errors.New(messages.GetErrorMessage())
	}
	defer queryResult.Close()

	lists := []model.TaskList{}
	if queryResult.Next() {
		list := model.TaskList{UserID: userID}
		if err = queryResult.Scan(
			&list.ID,
			&list.ListName,
			&list.ListDescription,
		); err != nil {
			return nil, err
		}
		lists = append(lists, list)
	} else {
		return nil, errors.New(messages.GetErrorMessageUserNotFound())
	}
	return lists, nil
}

// GetTaskListsByUser fetch all the tasks on the database by user id
func (repo TaskListRepository) GetTasksByUser(userID uint64) ([]model.Task, error) {
	queryResult, err := repo.db.Query(`SELECT 
	ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS, ID_LIST 
	FROM TASK WHERE ID_USER = ? ORDER BY ID_LIST`, userID)
	if err != nil {
		return nil, errors.New(messages.GetErrorMessage())
	}
	defer queryResult.Close()
	tasks := []model.Task{}
	if queryResult.Next() {
		task := model.Task{UserID: userID}
		if err = queryResult.Scan(
			&task.ID,
			&task.TaskName,
			&task.TaskDescription,
			&task.Deadline,
			&task.TaskStatus,
			&task.ListID,
		); err != nil {
			return nil, err
		}
		tasks = append(tasks, task)
	} else {
		return nil, errors.New(messages.GetErrorMessageUserNotFound())
	}
	return tasks, nil
}

// CreateNewList create a new list on the database for the user
func (repo TaskListRepository) CreateNewList(listName, listDescription string, userID uint64) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO LIST (LIST_NAME, LIST_DESCRIPTION, ID_USER)
		 VALUES (?, ?, ?)`,
	)
	if err != nil {
		return 0, err
	}
	defer statement.Close()
	result, err := statement.Exec(listName, listDescription, userID)
	if err != nil {
		return 0, errors.New(messages.GetErrorMessage())
	}
	newID, err := result.LastInsertId()
	if err != nil {
		return 0, err
	}
	return uint64(newID), nil
}

// UpdateList update list name and description on database
func (repo TaskListRepository) UpdateList(id uint64, listName, listDescription string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE LIST SET LIST_NAME = ?, LIST_DESCRIPTION = ? WHERE ID = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(listName, listDescription, id); erro != nil {
		return erro
	}
	return nil
}

// DeleteTasksFromList delete all the tasks from a list for the user
func (repo TaskListRepository) DeleteTasksFromList(listID, userID uint64) error {
	statement, erro := repo.db.Prepare(
		"DELETE FROM TASK WHERE ID_LIST = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	var list interface{} = nil
	if listID > 0 {
		list = listID
	}

	if _, erro = statement.Exec(list, userID); erro != nil {
		return erro
	}
	return nil
}

// DeleteList delete a list from the database for the user
func (repo TaskListRepository) DeleteList(listID, userID uint64) error {
	statement, erro := repo.db.Prepare(
		"DELETE FROM LIST WHERE ID = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(listID, userID); erro != nil {
		return erro
	}
	return nil
}

// CompleteTasksFromList update the status from the tasks to Complete on the list for the user
func (repo TaskListRepository) CompleteTasksFromList(listID, userID, taskStatus uint64) error {
	return repo.UpdateTaskStatusFromList(listID, userID, model.STATUS_TASK_COMPLETE)
}

// UncompleteTasksFromList update the status from the tasks to Incomplete on the list for the user
func (repo TaskListRepository) UncompleteTasksFromList(listID, userID, taskStatus uint64) error {
	return repo.UpdateTaskStatusFromList(listID, userID, model.STATUS_TASK_INCOMPLETE)
}

// UpdateTaskStatusFromList update the status from the tasks on the list for the user
func (repo TaskListRepository) UpdateTaskStatusFromList(listID, userID, taskStatus uint64) error {
	statement, erro := repo.db.Prepare(
		"UPDATE TASK SET TASK_STATUS = ? WHERE ID_LIST = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	var list interface{} = nil
	if listID > 0 {
		list = listID
	}

	if _, erro = statement.Exec(taskStatus, list, userID); erro != nil {
		return erro
	}
	return nil
}

// MoveTasksFromOneListToOther move tasks from one list to another
func (repo TaskListRepository) MoveTasksFromOneListToOther(userID, listIDOrigin, listIDDestiny uint64) error {
	statement, erro := repo.db.Prepare(
		"UPDATE TASK SET ID_LIST = ? WHERE ID_LIST = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}

	defer statement.Close()

	var idOrigin interface{} = nil
	if listIDOrigin > 0 {
		idOrigin = listIDOrigin
	}

	var idDestiny interface{} = nil
	if listIDDestiny > 0 {
		idDestiny = listIDDestiny
	}

	if _, erro = statement.Exec(idDestiny, idOrigin, userID); erro != nil {
		return erro
	}
	return nil
}

// MoveTasksToList update the tasks setting the new list id
func (repo TaskListRepository) MoveTasksToList(userID, listID uint64, tasksIDs []uint64) error {
	clauses := []string{"UPDATE TASK SET ID_LIST = ? WHERE ID_USER = ?"}
	params := make([]interface{}, 0)
	if len(tasksIDs) > 0 {
		clauses = append(clauses,
			fmt.Sprintf(
				"AND ID IN (%s)",
				strings.Join(strings.Split(strings.Repeat("?", len(tasksIDs)), ""), ", "),
			),
		)
		for _, taskID := range tasksIDs {
			params = append(params, taskID)
		}
	}
	statement, err := repo.db.Prepare(
		strings.Join(clauses, " "),
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	var list interface{} = nil
	if listID > 0 {
		list = listID
	}

	if _, err = statement.Exec(list, userID, params); err != nil {
		return err
	}
	return nil
}

// CreateTask create a new task for the user
func (repo TaskListRepository) CreateTask(taskName, taskDescription, deadline string, userID, listID uint64) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO TASK (TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS, ID_USER, ID_LIST)
		 VALUES (?, ?, ?, ?, ?, ?)`,
	)
	if err != nil {
		return 0, err
	}
	defer statement.Close()

	var list interface{} = nil
	if listID > 0 {
		list = listID
	}

	result, err := statement.Exec(taskName, taskDescription, deadline, model.STATUS_TASK_INCOMPLETE, userID, list)
	if err != nil {
		return 0, errors.New(messages.GetErrorMessage())
	}
	newID, err := result.LastInsertId()
	if err != nil {
		return 0, err
	}
	return uint64(newID), nil
}

// CreateTask create a new task for the user
func (repo TaskListRepository) UpdateTask(taskName, taskDescription, deadline string, userID, id uint64) error {
	statement, err := repo.db.Prepare(
		`UPDATE TASK SET TASK_NAME = ?, TASK_DESCRIPTION = ?, DEADLINE = ? WHERE ID_USER = ? AND ID = ?`,
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(taskName, taskDescription, deadline, userID, id); err != nil {
		return err
	}
	return nil

}

// CREATE TABLE IF NOT EXISTS LIST(
//     ID int auto_increment primary key,
//     LIST_NAME varchar(50) not null,
//     LIST_DESCRIPTION TEXT,
//     ID_USER int not null,
//     FOREIGN KEY (ID_USER)
//     REFERENCES USER(ID)
//     ON DELETE CASCADE
// ) ENGINE=INNODB;

// CREATE TABLE IF NOT EXISTS TASK(
//     ID int auto_increment primary key,
//     TASK_NAME varchar(50) not null,
//     TASK_DESCRIPTION TEXT,
//     DEADLINE DATE,
//     TASK_STATUS INT NOT NULL,
//     ID_USER int not null,
//     FOREIGN KEY (ID_USER)
//     REFERENCES USER(ID)
//     ON DELETE CASCADE,
//     ID_LIST int,
//     FOREIGN KEY (ID_LIST)
//     REFERENCES LIST(ID)
//     ON DELETE CASCADE
// ) ENGINE=INNODB;

// get tasks and lists - OK
// create list - OK
// update list - OK
// delete list and all tasks on that list - ok
// complete tasks from list - update status - ok
// uncomplete tasks from list - update status - ok
// move tasks to list - ok
// move tasks from list a to b - ok

// create task - ok
// update task - ok
// delete task -
// delete tasks -
// complete task - update status -
// uncomplete task - update status -
// complete tasks - update status -
// uncomplete tasks - update status -
// move task to another list -
