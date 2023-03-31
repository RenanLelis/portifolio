package business

import (
	"net/http"
	"strings"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/controller/response"
	"renan.com/todo/src/model"
	"renan.com/todo/src/persistence/dao"
)

// GetTasksAndLists return the lists and tasks for the user
func GetTasksAndLists(userID uint64) ([]response.TaskListDTO, Err) {
	if userID <= 0 {
		return nil, Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	taskLists, tasks, err := dao.GetTasksAndListsByUser(userID)
	if err != nil {
		return nil, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	taskListDTO := response.ConvertTasksAndListsToDTO(taskLists, tasks)
	return taskListDTO, Err{}
}

// CreateTaskList Create a new list for the user
func CreateTaskList(listName, listDescription string, userID uint64) (response.TaskListDTO, Err) {
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	if userID <= 0 || listNameFormatted == "" || len(listNameFormatted) <= 0 {
		return response.TaskListDTO{}, Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	newID, err := dao.CreateNewList(listNameFormatted, listDescriptionFormatted, userID)
	if err != nil {
		return response.TaskListDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	taskList := model.TaskList{
		ID:              newID,
		ListName:        listNameFormatted,
		ListDescription: listDescriptionFormatted,
		UserID:          userID,
	}
	return response.ConvertTaskListToDTO(taskList), Err{}
}

// UpdateTaskList update a list name and description for the user
func UpdateTaskList(id, userID uint64, listName, listDescription string) Err {
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	if userID <= 0 || id <= 0 || listNameFormatted == "" || len(listNameFormatted) <= 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.UpdateList(id, userID, listNameFormatted, listDescriptionFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// DeleteTaskList delete a list for the user
func DeleteTaskList(id, userID uint64) Err {
	if userID <= 0 || id <= 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.DeleteListAndTasks(id, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// MoveTasksForList change the list for the specifics tasks
func MoveTasksForList(idTasks []uint64, listID, userID uint64) Err {
	if userID <= 0 || idTasks == nil || len(idTasks) <= 0 || listID < 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.MoveTasksToList(userID, listID, idTasks)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// MoveTasksFromList change tasks from a list to another
func MoveTasksFromList(userID, listIDOrigin, listIDDestiny uint64) Err {
	if userID <= 0 || listIDOrigin < 0 || listIDDestiny < 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.MoveTasksFromOneListToOther(userID, listIDOrigin, listIDDestiny)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// CompleteTasksFromList set status to complete of all the tasks of a list
func CompleteTasksFromList(userID, listID uint64) Err {
	if userID <= 0 || listID < 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.CompleteTasksFromList(listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// UncompleteTasksFromList set status to incomplete of all the tasks of a list
func UncompleteTasksFromList(userID, listID uint64) Err {
	if userID <= 0 || listID < 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.UncompleteTasksFromList(listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// CreateTask create a new task on the database
func CreateTask(taskName, taskDescription, deadline string, userID, listID uint64) (response.TaskDTO, Err) {
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	deadlineFormatted := strings.TrimSpace(deadline)
	if userID <= 0 || listID < 0 || taskNameFormatted == "" || len(taskNameFormatted) <= 0 {
		return response.TaskDTO{}, Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	newID, err := dao.CreateTask(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, userID, listID)
	if err != nil {
		return response.TaskDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	task := model.Task{
		ID:              newID,
		TaskName:        taskNameFormatted,
		TaskDescription: taskDescriptionFormatted,
		Deadline:        deadlineFormatted,
		ListID:          listID,
		UserID:          userID,
	}
	return response.ConvertTaskToDTO(task), Err{}
}

// UpdateTask update a task on the database
func UpdateTask(taskName, taskDescription, deadline string, userID, id uint64) Err {
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	deadlineFormatted := strings.TrimSpace(deadline)
	if userID <= 0 || taskNameFormatted == "" || len(taskNameFormatted) <= 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	err := dao.UpdateTask(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, userID, id)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// MoveTaskToList change the list of one task
func MoveTaskToList(userID, listID, taskID uint64) Err {
	if userID <= 0 || taskID <= 0 || listID < 0 {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	tasks := make([]uint64, 0)
	tasks = append(tasks, taskID)
	return MoveTasksForList(tasks, listID, userID)
}

// DeleteTask delete a task from database
func DeleteTask(userID, taskID uint64) Err {
	if userID <= 0 || taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.DeleteTask(userID, taskID)
	if err != nil {
		return Err{Status: http.StatusInternalServerError, ErrorMessage: messages.GetErrorMessage()}
	}
	return Err{}
}

// DeleteTasks delete a group of tasks from database
func DeleteTasks(userID uint64, tasksIDs []uint64) Err {
	if userID <= 0 || tasksIDs == nil || len(tasksIDs) <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.DeleteTasks(tasksIDs, userID)
	if err != nil {
		return Err{Status: http.StatusInternalServerError, ErrorMessage: messages.GetErrorMessage()}
	}
	return Err{}
}

// CompleteTask update a task on database, set status to complete
func CompleteTask(userID, taskID uint64) Err {
	if userID <= 0 || taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.CompleteTask(userID, taskID)
	if err != nil {
		return Err{Status: http.StatusInternalServerError, ErrorMessage: messages.GetErrorMessage()}
	}
	return Err{}
}

// UncompleteTask update a task on database, set status to incomplete
func UncompleteTask(userID, taskID uint64) Err {
	if userID <= 0 || taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.UncompleteTask(userID, taskID)
	if err != nil {
		return Err{Status: http.StatusInternalServerError, ErrorMessage: messages.GetErrorMessage()}
	}
	return Err{}
}
