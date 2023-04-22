package repository

import (
	"database/sql"
	"errors"

	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
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

// GetTasksByUser fetch all the tasks on the database by user id
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
