package routes

import (
	"net/http"

	"renan.com/todo/src/controller"
)

var tasksRoutes = []Route{
	{
		URI:       "/api/taskList",
		Method:    http.MethodGet,
		Function:  controller.GetTasksAndLists,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/tasks/{id}",
		Method:    http.MethodGet,
		Function:  controller.GetTasksByList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/tasks",
		Method:    http.MethodGet,
		Function:  controller.GetTasksOnDefaultList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList",
		Method:    http.MethodPost,
		Function:  controller.CreateTaskList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/{id}",
		Method:    http.MethodPut,
		Function:  controller.UpdateTaskList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/{id}",
		Method:    http.MethodDelete,
		Function:  controller.DeleteTaskList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/tasks/moveFromList",
		Method:    http.MethodPut,
		Function:  controller.MoveTasksFromList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/tasks/complete/{id}",
		Method:    http.MethodPut,
		Function:  controller.CompleteTasksFromList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/taskList/tasks/uncomplete/{id}",
		Method:    http.MethodPut,
		Function:  controller.UncompleteTasksFromList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task",
		Method:    http.MethodPost,
		Function:  controller.CreateTask,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task/{id}",
		Method:    http.MethodPut,
		Function:  controller.UpdateTask,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task/list/{id}",
		Method:    http.MethodPut,
		Function:  controller.MoveTaskToList,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task/{id}",
		Method:    http.MethodDelete,
		Function:  controller.DeleteTask,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task/complete/{id}",
		Method:    http.MethodPut,
		Function:  controller.CompleteTask,
		NeedsAuth: true,
	},
	{
		URI:       "/api/task/uncomplete/{id}",
		Method:    http.MethodPut,
		Function:  controller.UncompleteTask,
		NeedsAuth: true,
	},
}
