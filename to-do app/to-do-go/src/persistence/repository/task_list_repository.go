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
	FROM TASK_LIST WHERE ID_USER = ?`, userID)
	if err != nil {
		return nil, errors.New(messages.GetErrorMessage())
	}
	defer queryResult.Close()

	lists := []model.TaskList{}
	for queryResult.Next() {
		list := model.TaskList{UserID: userID}

		if err = queryResult.Scan(
			&list.ID,
			&list.ListName,
			&list.ListDescription,
		); err != nil {
			return nil, err
		}
		lists = append(lists, list)
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
	for queryResult.Next() {
		task := model.Task{UserID: userID}
		var listID sql.NullInt64
		if err = queryResult.Scan(
			&task.ID,
			&task.TaskName,
			&task.TaskDescription,
			&task.Deadline,
			&task.TaskStatus,
			&listID,
		); err != nil {
			return nil, err
		}
		if listID.Valid {
			task.ListID = uint64(listID.Int64)
		}
		tasks = append(tasks, task)
	}
	return tasks, nil
}

// GetTasksByList fetch the tasks for a specific list
func (repo TaskListRepository) GetTasksByList(userID, listID uint64) ([]model.Task, error) {
	var queryResult *sql.Rows
	var err error
	if listID > 0 {
		queryResult, err = repo.db.Query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS FROM TASK WHERE ID_USER = ? AND ID_LIST = ?`, userID, listID)
	} else {
		queryResult, err = repo.db.Query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS FROM TASK WHERE ID_USER = ? AND ID_LIST IS NULL`, userID)
	}
	if err != nil {
		return nil, errors.New(messages.GetErrorMessage())
	}
	defer queryResult.Close()
	tasks := []model.Task{}
	for queryResult.Next() {
		task := model.Task{UserID: userID}
		if err = queryResult.Scan(
			&task.ID,
			&task.TaskName,
			&task.TaskDescription,
			&task.Deadline,
			&task.TaskStatus,
		); err != nil {
			return nil, err
		}
		task.ListID = listID
		tasks = append(tasks, task)
	}
	return tasks, nil
}

// CreateNewList create a new list on the database for the user
func (repo TaskListRepository) CreateNewList(listName, listDescription string, userID uint64) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO TASK_LIST (LIST_NAME, LIST_DESCRIPTION, ID_USER)
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
func (repo TaskListRepository) UpdateList(id, userID uint64, listName, listDescription string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE TASK_LIST SET LIST_NAME = ?, LIST_DESCRIPTION = ? WHERE ID = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(listName, listDescription, id, userID); erro != nil {
		return erro
	}
	return nil
}

// DeleteTasksFromList delete all the tasks from a list for the user
func (repo TaskListRepository) DeleteTasksFromList(listID, userID uint64) error {
	sqlStr := "DELETE FROM TASK WHERE ID_LIST = ? AND ID_USER = ?"
	if listID <= 0 {
		sqlStr = "DELETE FROM TASK WHERE ID_LIST IS NULL AND ID_USER = ?"
	}
	statement, erro := repo.db.Prepare(sqlStr)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if listID <= 0 {
		_, erro = statement.Exec(userID)
	} else {
		_, erro = statement.Exec(listID, userID)
	}

	if erro != nil {
		return erro
	}
	return nil
}

// DeleteList delete a list from the database for the user
func (repo TaskListRepository) DeleteList(listID, userID uint64) error {
	statement, erro := repo.db.Prepare("DELETE FROM TASK_LIST WHERE ID = ? AND ID_USER = ?")
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
func (repo TaskListRepository) CompleteTasksFromList(listID, userID uint64) error {
	return repo.UpdateTaskStatusFromList(listID, userID, model.STATUS_TASK_COMPLETE)
}

// UncompleteTasksFromList update the status from the tasks to Incomplete on the list for the user
func (repo TaskListRepository) UncompleteTasksFromList(listID, userID uint64) error {
	return repo.UpdateTaskStatusFromList(listID, userID, model.STATUS_TASK_INCOMPLETE)
}

// UpdateTaskStatusFromList update the status from the tasks on the list for the user
func (repo TaskListRepository) UpdateTaskStatusFromList(listID, userID, taskStatus uint64) error {
	sqlStr := "UPDATE TASK SET TASK_STATUS = ? WHERE ID_LIST = ? AND ID_USER = ?"
	if listID <= 0 {
		sqlStr = "UPDATE TASK SET TASK_STATUS = ? WHERE ID_LIST IS NULL AND ID_USER = ?"
	}
	statement, err := repo.db.Prepare(sqlStr)
	if err != nil {
		return err
	}
	defer statement.Close()

	if listID <= 0 {
		_, err = statement.Exec(taskStatus, userID)
	} else {
		_, err = statement.Exec(taskStatus, listID, userID)
	}

	if err != nil {
		return err
	}
	return nil
}

// MoveTasksFromOneListToOther move tasks from one list to another
func (repo TaskListRepository) MoveTasksFromOneListToOther(userID, listIDOrigin, listIDDestiny uint64) error {
	sqlStr := "UPDATE TASK SET ID_LIST = ? WHERE ID_LIST = ? AND ID_USER = ?"
	if listIDOrigin <= 0 {
		sqlStr = "UPDATE TASK SET ID_LIST = ? WHERE ID_LIST IS NULL AND ID_USER = ?"
	}
	statement, err := repo.db.Prepare(sqlStr)
	if err != nil {
		return err
	}

	defer statement.Close()

	var idDestiny interface{} = nil
	if listIDDestiny > 0 {
		idDestiny = listIDDestiny
	}

	if listIDOrigin <= 0 {
		_, err = statement.Exec(idDestiny, userID)
	} else {
		_, err = statement.Exec(idDestiny, listIDOrigin, userID)
	}

	if err != nil {
		return err
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

// UpdateTask update the task data for the user
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

// DeleteTask delete a task from the database
func (repo TaskListRepository) DeleteTask(taskID, userID uint64) error {
	statement, erro := repo.db.Prepare(
		"DELETE FROM TASK WHERE ID = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(taskID, userID); erro != nil {
		return erro
	}
	return nil
}

// DeleteTasks delete the tasks from the database
func (repo TaskListRepository) DeleteTasks(tasksIDs []uint64, userID uint64) error {
	clauses := []string{"DELETE FROM TASK WHERE ID_USER = ? "}
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

	if _, err = statement.Exec(userID, params); err != nil {
		return err
	}
	return nil
}

// CompleteTask update a task status to complete
func (repo TaskListRepository) CompleteTask(userID, id uint64) error {
	return repo.SetStatusTask(userID, id, model.STATUS_TASK_COMPLETE)
}

// UncompleteTask update a task status to incomplete
func (repo TaskListRepository) UncompleteTask(userID, id uint64) error {
	return repo.SetStatusTask(userID, id, model.STATUS_TASK_INCOMPLETE)
}

// SetStatusTask update a task status
func (repo TaskListRepository) SetStatusTask(userID, id, taskStatus uint64) error {
	statement, err := repo.db.Prepare(
		`UPDATE TASK SET TASK_STATUS = ? WHERE ID_USER = ? AND ID = ?`,
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(taskStatus, userID, id); err != nil {
		return err
	}
	return nil
}
