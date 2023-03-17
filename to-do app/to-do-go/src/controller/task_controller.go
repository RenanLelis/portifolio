package controller

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"strconv"

	"github.com/gorilla/mux"
	"renan.com/todo/src/controller/form"
	"renan.com/todo/src/controller/response"
)

// GetTasksAndLists implements the controller to get tasks and lists
func GetTasksAndLists(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// CreateTaskList implements the controller to create a new lists
func CreateTaskList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// UpdateTaskList implements the controller to update a list data
func UpdateTaskList(w http.ResponseWriter, r *http.Request) {
	param := mux.Vars(r)
	listID, err := strconv.ParseUint(param["id"], 10, 64)
	if err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
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

	fmt.Println(listID)
	//TODO
}

// DeleteTaskList implements the controller to delete a list and it's tasks
func DeleteTaskList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// MoveTasksForList implements the controller to change tasks to another list
func MoveTasksForList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// MoveTasksFromList implements the controller to change tasks from one list to another
func MoveTasksFromList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// CompleteTasksFromList implements the controller to change an array of tasks status to complete
func CompleteTasksFromList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// UncompleteTasksFromList implements the controller to change an array of tasks status to incomplete
func UncompleteTasksFromList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// CreateTask implements the controller to create a new task
func CreateTask(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// UpdateTask implements the controller to update a task
func UpdateTask(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// MoveTaskToList implements the controller to update a task list (move one task to other list)
func MoveTaskToList(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// DeleteTask implements the controller to delete a task
func DeleteTask(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// CompleteTask implements the controller to update a task status to complete
func CompleteTask(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// UncompleteTask implements the controller to update a task status to incomplete
func UncompleteTask(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// DeleteTasks implements the controller to delete an array of tasks
func DeleteTasks(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// CompleteTasks implements the controller to update an array of tasks, set status to complete
func CompleteTasks(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// UncompleteTasks implements the controller to update an array of tasks, set status to incomplete
func UncompleteTasks(w http.ResponseWriter, r *http.Request) {
	//TODO
}

// {
// 	URI:       "/api/taskList/{id}",
// 	Method:    http.MethodPut,
// 	Function:  controller.UpdateTaskList,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/taskList/{id}",
// 	Method:    http.MethodDelete,
// 	Function:  controller.DeleteTaskList,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/task/{id}",
// 	Method:    http.MethodPut,
// 	Function:  controller.UpdateTask,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/task/list/{id}",
// 	Method:    http.MethodPut,
// 	Function:  controller.MoveTaskToList,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/task/{id}",
// 	Method:    http.MethodDelete,
// 	Function:  controller.DeleteTask,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/task/complete/{id}",
// 	Method:    http.MethodPut,
// 	Function:  controller.CompleteTask,
// 	NeedsAuth: true,
// },
// {
// 	URI:       "/api/task/uncomplete/{id}",
// 	Method:    http.MethodPut,
// 	Function:  controller.UncompleteTask,
// 	NeedsAuth: true,
// },
