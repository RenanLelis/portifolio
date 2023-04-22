package business

import (
	"errors"
	"net/http"
	"strings"
	"time"

	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/controller/response"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
)

// GetTasksAndLists implements the controller to get tasks and lists
func GetTasksAndLists(userID uint64) ([]response.TaskListDTO, Err) {
	if userID <= 0 {
		return nil, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return nil, Err{}
}

// GetLists implements the controller to get lists
func GetLists(userID uint64) ([]response.TaskListDTO, Err) {
	if userID <= 0 {
		return nil, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return nil, Err{}
}

// GetTasksByList return tasks by a selected list
func GetTasksByList(listID, userID uint64) ([]response.TaskDTO, Err) {
	if listID <= 0 || userID <= 0 {
		return nil, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return nil, Err{}
}

// CreateTaskList create a new list for the user on the database and return the list created
func CreateTaskList(listName, listDescription string, userID uint64) (response.TaskListDTO, Err) {
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	err := validateTaskListData(listNameFormatted, listDescriptionFormatted, userID)
	if err != nil {
		return response.TaskListDTO{}, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return response.TaskListDTO{}, Err{}
}

// validateTaskListData validate the input data for taskList Creation
func validateTaskListData(listName, listDescription string, userID uint64) error {
	if len(strings.TrimSpace(listName)) <= 0 || userID <= 0 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// UpdateTaskList update a list for the user on the database
func UpdateTaskList(listName, listDescription string, listID, userID uint64) Err {
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	if err := validateTaskListData(listNameFormatted, listDescriptionFormatted, userID); err != nil || listID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// DeleteTaskList delete a list for the user and the tasks related
func DeleteTaskList(listID, userID uint64) Err {
	if listID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// CompleteTasksFromList mark as complete all tasks from a list
func CompleteTasksFromList(listID, userID uint64) Err {
	if listID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// UncompleteTasksFromList mark as incomplete all tasks from a list
func UncompleteTasksFromList(listID, userID uint64) Err {
	if listID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// MoveTasksFromList move the tasks from a list to another
func MoveTasksFromList(listIDOrigin, listIDDestiny, userID uint64) Err {
	if listIDOrigin <= 0 || listIDDestiny <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// CreateTask create a new task for the user on the database and return the task created
func CreateTask(taskName, taskDescription, deadline string, listID, userID uint64) (response.TaskDTO, Err) {
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	if err := validateTaskData(taskNameFormatted, taskDescriptionFormatted, deadline, userID); err != nil || listID <= 0 {
		return response.TaskDTO{}, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return response.TaskDTO{}, Err{}
}

// validateTaskData validate the input data for task Creation
func validateTaskData(taskName, taskDescription, deadline string, userID uint64) error {
	if len(strings.TrimSpace(taskName)) <= 0 || userID <= 0 || IsDeadlineInvalid(deadline) {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// IsDeadlineInvalid validate the deadlineInput
func IsDeadlineInvalid(deadline string) bool {
	deadlineFormatted := strings.TrimSpace(deadline)
	if len(deadlineFormatted) == 0 {
		return false
	}
	if len(deadlineFormatted) != 10 {
		return true
	}
	_, err := time.Parse("01/02/2006", deadline)
	return err != nil
}

// UpdateTask update a task for the user on the database
func UpdateTask(taskName, taskDescription, deadline string, taskID, userID uint64) Err {
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	if err := validateTaskData(taskNameFormatted, taskDescriptionFormatted, deadline, userID); err != nil {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// MoveTaskToList update a task for the user on the database, changing the listID
func MoveTaskToList(taskID, listIDDestiny, userID uint64) Err {
	if taskID <= 0 || listIDDestiny <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// DeleteTask delete a task for the user on the database
func DeleteTask(taskID, userID uint64) Err {
	if taskID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// CompleteTask mark a task as complete for the user on the database
func CompleteTask(taskID, userID uint64) Err {
	if taskID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// UncompleteTask mark a task as incomplete for the user on the database
func UncompleteTask(taskID, userID uint64) Err {
	if taskID <= 0 || userID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	//TODO
	return Err{}
}

// CreateDefaultList create a default object of tasklist for a new user creation
func CreateDefaultList(userID uint64) model.TaskList {
	return model.TaskList{
		ListName:        "My Tasks",
		ListDescription: "Default list for the user, created by the app",
		UserID:          userID,
	}
}
