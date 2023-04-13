package controller

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"renan.com/todo/src/business"
	"renan.com/todo/src/controller/form"
	"renan.com/todo/src/controller/response"
	"renan.com/todo/src/security"
)

// GetTasksAndLists implements the controller to get tasks and lists
func GetTasksAndLists(w http.ResponseWriter, r *http.Request) {
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	tasksListsDTO, bErr := business.GetTasksAndLists(userID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, tasksListsDTO, r)
}

// GetTasksByList implements the controller to get tasks by a selected list
func GetTasksByList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	taskDTOs, bErr := business.GetTasksByList(userID, listID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, taskDTOs, r)
}

// GetTasksOnDefaultList implements the controller to get tasks not related to a list
func GetTasksOnDefaultList(w http.ResponseWriter, r *http.Request) {
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	taskDTOs, bErr := business.GetTasksByList(userID, 0)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, taskDTOs, r)
}

// CreateTaskList implements the controller to create a new lists
func CreateTaskList(w http.ResponseWriter, r *http.Request) {
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.TaskListForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	taskListDTO, bErr := business.CreateTaskList(form.ListName, form.ListDescription, userID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusCreated, taskListDTO, r)
}

// UpdateTaskList implements the controller to update a list data
func UpdateTaskList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.TaskListForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.UpdateTaskList(listID, userID, form.ListName, form.ListDescription)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// DeleteTaskList implements the controller to delete a list and it's tasks
func DeleteTaskList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.DeleteTaskList(listID, userID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// MoveTasksFromList implements the controller to change tasks from one list to another
func MoveTasksFromList(w http.ResponseWriter, r *http.Request) {
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.MoveTasksFromListForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.MoveTasksFromList(userID, form.ListIDOrigin, form.ListIDDestiny)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// CompleteTasksFromList implements the controller to change an array of tasks status to complete
func CompleteTasksFromList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.CompleteTasksFromList(userID, listID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// UncompleteTasksFromList implements the controller to change an array of tasks status to incomplete
func UncompleteTasksFromList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.UncompleteTasksFromList(userID, listID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// CreateTask implements the controller to create a new task
func CreateTask(w http.ResponseWriter, r *http.Request) {
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.TaskForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	taskDTO, bErr := business.CreateTask(form.TaskName, form.TaskDescription, form.Deadline, userID, form.ListID)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusCreated, taskDTO, r)
}

// UpdateTask implements the controller to update a task
func UpdateTask(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	id, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.TaskForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.UpdateTask(form.TaskName, form.TaskDescription, form.Deadline, userID, id)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// MoveTaskToList implements the controller to update a task list (move one task to other list)
func MoveTaskToList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	id, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.MoveTaskToListForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.MoveTaskToList(userID, form.ListID, id)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// DeleteTask implements the controller to delete a task
func DeleteTask(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	id, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.DeleteTask(userID, id)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// CompleteTask implements the controller to update a task status to complete
func CompleteTask(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	id, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.CompleteTask(userID, id)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// UncompleteTask implements the controller to update a task status to incomplete
func UncompleteTask(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	id, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusUnauthorized, err, r)
		return
	}
	bErr := business.UncompleteTask(userID, id)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}
