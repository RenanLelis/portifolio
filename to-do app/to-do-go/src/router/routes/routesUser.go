package routes

import "net/http"

var userRoutes = []Route{
	{
		URI:    "/api/user/profile",
		Method: http.MethodPut,
		// Function:             controller.UpdateUserProfile,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/user/password",
		Method: http.MethodPut,
		// Function:             controller.UpdateUserPassword,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
}
