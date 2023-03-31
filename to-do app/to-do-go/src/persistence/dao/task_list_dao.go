package dao

import (
	"renan.com/todo/src/database"
	"renan.com/todo/src/model"
	"renan.com/todo/src/persistence/repository"
)

// GetTasksAndListsByUser fetch the lists and the tasks by user id
func GetTasksAndListsByUser(userID uint64) ([]model.TaskList, []model.Task, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return nil, nil, err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	lists, err := rep.GetTaskListsByUser(userID)
	if err != nil {
		return nil, nil, err
	}
	tasks, err := rep.GetTasksByUser(userID)
	if err != nil {
		return nil, nil, err
	}
	return lists, tasks, nil
}

// GetTaskListsByUser fetch the lists on the database by user id
func GetTaskListsByUser(userID uint64) ([]model.TaskList, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return nil, err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.GetTaskListsByUser(userID)
}

// GetTaskListsByUser fetch all the tasks on the database by user id
func GetTasksByUser(userID uint64) ([]model.Task, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return nil, err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.GetTasksByUser(userID)
}

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

// UpdateList update list name and description on database
func UpdateList(id, userID uint64, listName, listDescription string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.UpdateList(id, userID, listName, listDescription)
}

// DeleteTasksFromList delete all the tasks from a list for the user
func DeleteTasksFromList(listID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.DeleteTasksFromList(listID, userID)
}

// DeleteList delete a list from the database for the user
func DeleteList(listID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.DeleteList(listID, userID)
}

// DeleteList delete a list ands its tasks from the database for the user
func DeleteListAndTasks(listID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	err = rep.DeleteTasksFromList(listID, userID)
	if err != nil {
		return err
	}
	err = rep.DeleteList(listID, userID)
	if err != nil {
		return err
	}
	return nil
}

// CompleteTasksFromList update the status from the tasks to Complete on the list for the user
func CompleteTasksFromList(listID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.CompleteTasksFromList(listID, userID)
}

// UncompleteTasksFromList update the status from the tasks to Incomplete on the list for the user
func UncompleteTasksFromList(listID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.UncompleteTasksFromList(listID, userID)
}

// MoveTasksFromOneListToOther move tasks from one list to another
func MoveTasksFromOneListToOther(userID, listIDOrigin, listIDDestiny uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.MoveTasksFromOneListToOther(userID, listIDOrigin, listIDDestiny)
}

// MoveTasksToList update the tasks setting the new list id
func MoveTasksToList(userID, listID uint64, tasksIDs []uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.MoveTasksToList(userID, listID, tasksIDs)
}

// CreateTask create a new task for the user
func CreateTask(taskName, taskDescription, deadline string, userID, listID uint64) (uint64, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return 0, err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.CreateTask(taskName, taskDescription, deadline, userID, listID)
}

// UpdateTask update the task data for the user
func UpdateTask(taskName, taskDescription, deadline string, userID, id uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.UpdateTask(taskName, taskDescription, deadline, userID, id)
}

// DeleteTask delete a task from the database
func DeleteTask(taskID, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.DeleteTask(taskID, userID)
}

// DeleteTasks delete the tasks from the database
func DeleteTasks(tasksIDs []uint64, userID uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.DeleteTasks(tasksIDs, userID)
}

// CompleteTask update a task status to complete
func CompleteTask(userID, id uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.CompleteTask(userID, id)
}

// UncompleteTask update a task status to incomplete
func UncompleteTask(userID, id uint64) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateTaskListRepository(db)
	return rep.UncompleteTask(userID, id)
}
