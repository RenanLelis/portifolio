package dao

import (
	"renanlelis.github.io/portfolio/to-do-go/src/database"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
	"renanlelis.github.io/portfolio/to-do-go/src/persistence/repository"
)

// CreateNewList create a new list on the database for the user
func CreateNewList(listName, listDescription string, userID uint64) (uint64, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return 0, err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.CreateNewList(listName, listDescription, userID)
}

// GetTasksAndLists implements the controller to get tasks and lists
func GetTasksAndLists(userID uint64) ([]model.TaskList, []model.Task, error) {
	//TODO
	return nil, nil, nil
}

// GetLists implements the controller to get lists
func GetLists(userID uint64) ([]model.TaskList, error) {
	//TODO
	return nil, nil
}

// GetTasksByList return tasks by a selected list
func GetTasksByList(listID, userID uint64) ([]model.TaskList, error) {
	//TODO
	return nil, nil
}

// UpdateTaskList update a list for the user on the database
func UpdateTaskList(listName, listDescription string, listID, userID uint64) error {
	//TODO
	return nil
}

// DeleteTaskList delete a list for the user and the tasks related
func DeleteTaskList(listID, userID uint64) error {
	//TODO
	return nil
}

// CompleteTasksFromList mark as complete all tasks from a list
func CompleteTasksFromList(listID, userID uint64) error {
	//TODO
	return nil
}

// UncompleteTasksFromList mark as incomplete all tasks from a list
func UncompleteTasksFromList(listID, userID uint64) error {
	//TODO
	return nil
}

// MoveTasksFromList move the tasks from a list to another
func MoveTasksFromList(listIDOrigin, listIDDestiny, userID uint64) error {
	//TODO
	return nil
}

// CreateTask create a new task for the user on the database and return the task created
func CreateTask(taskName, taskDescription, deadline string, listID, userID uint64) (uint64, error) {
	//TODO
	return 0, nil
}

// UpdateTask update a task for the user on the database
func UpdateTask(taskName, taskDescription, deadline string, taskID, userID uint64) error {
	//TODO
	return nil
}

// MoveTaskToList update a task for the user on the database, changing the listID
func MoveTaskToList(taskID, listIDDestiny, userID uint64) error {
	//TODO
	return nil
}

// DeleteTask delete a task for the user on the database
func DeleteTask(taskID, userID uint64) error {
	//TODO
	return nil
}

// CompleteTask mark a task as complete for the user on the database
func CompleteTask(taskID, userID uint64) error {
	//TODO
	return nil
}

// UncompleteTask mark a task as incomplete for the user on the database
func UncompleteTask(taskID, userID uint64) error {
	//TODO
	return nil
}
