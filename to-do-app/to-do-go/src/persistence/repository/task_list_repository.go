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
func (repo TaskListRepository) GetTasksByList(listID, userID uint64) ([]model.Task, error) {
	queryResult, err := repo.db.Query(`SELECT ID, TASK_NAME, TASK_DESCRIPTION, DEADLINE, TASK_STATUS 
	FROM TASK WHERE ID_USER = ? AND ID_LIST = ?`, userID, listID)
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
	statement, erro := repo.db.Prepare("DELETE FROM TASK WHERE ID_LIST = ? AND ID_USER = ?")
	if erro != nil {
		return erro
	}
	defer statement.Close()
	if _, erro = statement.Exec(listID, userID); erro != nil {
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

// CompleteTasksFromList mark as complete all tasks from a list on the database
func (repo TaskListRepository) CompleteTasksFromList(listID, userID uint64) error {
	return repo.UpdateStatusTasksFromList(listID, userID, model.STATUS_TASK_COMPLETE)
}

// UncompleteTasksFromList mark as incomplete all tasks from a list on the database
func (repo TaskListRepository) UncompleteTasksFromList(listID, userID uint64) error {
	return repo.UpdateStatusTasksFromList(listID, userID, model.STATUS_TASK_INCOMPLETE)
}

// UpdateStatusTasksFromList mark as complete or incomplete all tasks from a list on the database
func (repo TaskListRepository) UpdateStatusTasksFromList(listID, userID, taskStatus uint64) error {
	statement, err := repo.db.Prepare("UPDATE TASK SET TASK_STATUS = ? WHERE ID_LIST = ? AND ID_USER = ?")
	if err != nil {
		return err
	}
	defer statement.Close()
	if _, err = statement.Exec(taskStatus, listID, userID); err != nil {
		return err
	}
	return nil
}

// MoveTasksFromList move the tasks from a list to another
func (repo TaskListRepository) MoveTasksFromList(listIDOrigin, listIDDestiny, userID uint64) error {
	statement, erro := repo.db.Prepare("UPDATE TASK SET ID_LIST = ? WHERE ID_LIST = ? AND ID_USER = ?")
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(listIDDestiny, listIDOrigin, userID); erro != nil {
		return erro
	}
	return nil
}

// CreateTask create a new task for the user on the database and return the id of the task created
func (repo TaskListRepository) CreateTask(taskName, taskDescription, deadline string, listID, userID uint64) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO TASK (TASK_NAME, TASK_DESCRIPTION, DEADLINE, ID_LIST, ID_USER, TASK_STATUS)
		 VALUES (?, ?, ?, ?, ?, ?)`,
	)
	if err != nil {
		return 0, err
	}
	defer statement.Close()
	result, err := statement.Exec(taskName, taskDescription, deadline, listID, userID, model.STATUS_TASK_INCOMPLETE)
	if err != nil {
		return 0, errors.New(messages.GetErrorMessage())
	}
	newID, err := result.LastInsertId()
	if err != nil {
		return 0, err
	}
	return uint64(newID), nil
}

// UpdateTask update a task for the user on the database
func (repo TaskListRepository) UpdateTask(taskName, taskDescription, deadline string, taskID, userID uint64) error {
	statement, erro := repo.db.Prepare(
		"UPDATE TASK SET TASK_NAME = ?, TASK_DESCRIPTION = ?, DEADLINE = ? WHERE ID = ? AND ID_USER = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(taskName, taskDescription, deadline, taskID, userID); erro != nil {
		return erro
	}
	return nil
}

// MoveTaskToList update a task for the user on the database, changing the listID
func (repo TaskListRepository) MoveTaskToList(taskID, listIDDestiny, userID uint64) error {
	statement, erro := repo.db.Prepare("UPDATE TASK SET ID_LIST = ? WHERE ID = ? AND ID_USER = ?")
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(listIDDestiny, taskID, userID); erro != nil {
		return erro
	}
	return nil
}

// DeleteTask delete a task for the user on the database
func (repo TaskListRepository) DeleteTask(taskID, userID uint64) error {
	statement, erro := repo.db.Prepare("DELETE FROM TASK WHERE ID = ? AND ID_USER = ?")
	if erro != nil {
		return erro
	}
	defer statement.Close()
	if _, erro = statement.Exec(taskID, userID); erro != nil {
		return erro
	}
	return nil
}

// CompleteTask mark a task as complete for the user on the database
func (repo TaskListRepository) CompleteTask(taskID, userID uint64) error {
	return repo.updateStatusTask(taskID, userID, model.STATUS_TASK_COMPLETE)
}

// UncompleteTask mark a task as incomplete for the user on the database
func (repo TaskListRepository) UncompleteTask(taskID, userID uint64) error {
	return repo.updateStatusTask(taskID, userID, model.STATUS_TASK_INCOMPLETE)
}

func (repo TaskListRepository) updateStatusTask(taskID, userID, status uint64) error {
	statement, erro := repo.db.Prepare("UPDATE TASK SET TASK_STATUS = ? WHERE ID = ? AND ID_USER = ?")
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(status, taskID, userID); erro != nil {
		return erro
	}
	return nil
}
