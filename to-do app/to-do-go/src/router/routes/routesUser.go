package routes

import (
	"net/http"

	"renan.com/todo/src/controller"
)

var userRoutes = []Route{
	{
		URI:       "/api/user/profile",
		Method:    http.MethodPut,
		Function:  controller.UpdateUserProfile,
		NeedsAuth: true,
	},
	{
		URI:       "/api/user/password",
		Method:    http.MethodPut,
		Function:  controller.UpdateUserPassword,
		NeedsAuth: true,
	},
}
