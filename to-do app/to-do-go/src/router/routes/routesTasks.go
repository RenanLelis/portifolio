package routes

import "net/http"

var tasksRoutes = []Route{
	{
		URI:    "/api/taskList",
		Method: http.MethodGet,
		// Function:             controller.GetTasksAndLists,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList",
		Method: http.MethodPost,
		// Function:             controller.CreateTaskList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/{id}",
		Method: http.MethodPut,
		// Function:             controller.UpdateTaskList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/{id}",
		Method: http.MethodDelete,
		// Function:             controller.DeleteTaskList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/tasks/move",
		Method: http.MethodPut,
		// Function:             controller.MoveTasksForList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/tasks/moveFromList",
		Method: http.MethodPut,
		// Function:             controller.MoveTasksFromList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/tasks/complete",
		Method: http.MethodPut,
		// Function:             controller.CompleteTasksFromList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/taskList/tasks/uncomplete",
		Method: http.MethodPut,
		// Function:             controller.UncompleteTasksFromList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task",
		Method: http.MethodPost,
		// Function:             controller.CreateTask,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task/{id}",
		Method: http.MethodPut,
		// Function:             controller.UpdateTask,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task/list/{id}",
		Method: http.MethodPut,
		// Function:             controller.MoveTaskToList,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task/{id}",
		Method: http.MethodDelete,
		// Function:             controller.DeleteTask,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task/complete/{id}",
		Method: http.MethodPut,
		// Function:             controller.CompleteTask,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/task/uncomplete/{id}",
		Method: http.MethodPut,
		// Function:             controller.UncompleteTask,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/tasks",
		Method: http.MethodDelete,
		// Function:             controller.DeleteTasks,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/tasks/complete",
		Method: http.MethodPut,
		// Function:             controller.CompleteTasks,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/tasks/uncomplete",
		Method: http.MethodPut,
		// Function:             controller.UncompleteTasks,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
}