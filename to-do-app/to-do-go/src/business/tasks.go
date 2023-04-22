package business

import (
	"errors"
	"net/http"
	"strings"
	"time"

	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/controller/response"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
	"renanlelis.github.io/portfolio/to-do-go/src/persistence/dao"
)

// GetTasksAndLists get tasks and lists for the user
func GetTasksAndLists(userID uint64) ([]response.TaskListDTO, Err) {
	if userID <= 0 {
		return nil, Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	lists, tasks, err := dao.GetTasksAndLists(userID)
	if err != nil {
		return nil, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	taskListDTO := response.ConvertTasksAndListsToDTO(lists, tasks)
	return taskListDTO, Err{}
}

// GetLists get lists for the user
func GetLists(userID uint64) ([]response.TaskListDTO, Err) {
	if userID <= 0 {
		return nil, Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	lists, err := dao.GetLists(userID)
	if err != nil {
		return nil, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	taskListDTO := response.ConvertTasksAndListsToDTO(lists, make([]model.Task, 0))
	return taskListDTO, Err{}
}

// GetTasksByList return tasks by a selected list
func GetTasksByList(listID, userID uint64) ([]response.TaskDTO, Err) {
	if userID <= 0 {
		return nil, Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if listID <= 0 {
		return nil, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	tasks, err := dao.GetTasksByList(listID, userID)
	if err != nil {
		return nil, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	tasksDTO := response.ConvertTasksToDTO(tasks)
	return tasksDTO, Err{}
}

// CreateTaskList create a new list for the user on the database and return the list created
func CreateTaskList(listName, listDescription string, userID uint64) (response.TaskListDTO, Err) {
	if userID <= 0 {
		return response.TaskListDTO{}, Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	err := validateTaskListData(listNameFormatted, listDescriptionFormatted, userID)
	if err != nil {
		return response.TaskListDTO{}, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
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
	taskListDTO := response.ConvertTaskListToDTO(taskList)
	return taskListDTO, Err{}
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
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	listNameFormatted := strings.TrimSpace(listName)
	listDescriptionFormatted := strings.TrimSpace(listDescription)
	if err := validateTaskListData(listNameFormatted, listDescriptionFormatted, userID); err != nil || listID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.UpdateTaskList(listNameFormatted, listDescriptionFormatted, listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// DeleteTaskList delete a list for the user and the tasks related
func DeleteTaskList(listID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if listID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	lists, err := dao.GetLists(userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if lists == nil || len(lists) <= 1 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageCannotDeleteOnlyList()}
	}
	err = dao.DeleteTaskList(listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// CompleteTasksFromList mark as complete all tasks from a list
func CompleteTasksFromList(listID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if listID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.CompleteTasksFromList(listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// UncompleteTasksFromList mark as incomplete all tasks from a list
func UncompleteTasksFromList(listID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if listID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.UncompleteTasksFromList(listID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// MoveTasksFromList move the tasks from a list to another
func MoveTasksFromList(listIDOrigin, listIDDestiny, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if listIDOrigin <= 0 || listIDDestiny <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.MoveTasksFromList(listIDOrigin, listIDDestiny, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// CreateTask create a new task for the user on the database and return the task created
func CreateTask(taskName, taskDescription, deadline string, listID, userID uint64) (response.TaskDTO, Err) {
	if userID <= 0 {
		return response.TaskDTO{}, Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	deadlineFormatted := strings.TrimSpace(deadline)
	if err := validateTaskData(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, userID); err != nil || listID <= 0 {
		return response.TaskDTO{}, Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	newID, err := dao.CreateTask(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, listID, userID)
	if err != nil {
		return response.TaskDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	task := model.Task{
		ID:              newID,
		TaskName:        taskNameFormatted,
		TaskDescription: taskDescriptionFormatted,
		Deadline:        deadlineFormatted,
		TaskStatus:      model.STATUS_TASK_INCOMPLETE,
		ListID:          listID,
		UserID:          userID,
	}
	taskDTO := response.ConvertTaskToDTO(task)
	return taskDTO, Err{}
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
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	taskNameFormatted := strings.TrimSpace(taskName)
	taskDescriptionFormatted := strings.TrimSpace(taskDescription)
	deadlineFormatted := strings.TrimSpace(deadline)
	if err := validateTaskData(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, userID); err != nil {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.UpdateTask(taskNameFormatted, taskDescriptionFormatted, deadlineFormatted, taskID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// MoveTaskToList update a task for the user on the database, changing the listID
func MoveTaskToList(taskID, listIDDestiny, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if taskID <= 0 || listIDDestiny <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.MoveTaskToList(taskID, listIDDestiny, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// DeleteTask delete a task for the user on the database
func DeleteTask(taskID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.DeleteTask(taskID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// CompleteTask mark a task as complete for the user on the database
func CompleteTask(taskID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.CompleteTask(taskID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// UncompleteTask mark a task as incomplete for the user on the database
func UncompleteTask(taskID, userID uint64) Err {
	if userID <= 0 {
		return Err{Status: http.StatusUnauthorized, ErrorMessage: messages.GetErrorMessageToken()}
	}
	if taskID <= 0 {
		return Err{Status: http.StatusBadRequest, ErrorMessage: messages.GetErrorMessageInputValues()}
	}
	err := dao.UncompleteTask(taskID, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
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
