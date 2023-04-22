package routes

import (
	"net/http"

	"renanlelis.github.io/portfolio/to-do-go/src/controller"
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
