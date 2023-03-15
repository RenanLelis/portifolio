package routes

import "net/http"

var userRoutes = []Route{
	{
		URI:    "/api/user/profile",
		Method: http.MethodPut,
		// Function:             controller.updateUserProfile,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
	{
		URI:    "/api/user/password",
		Method: http.MethodPut,
		// Function:             controller.updateUserPassword,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: true,
	},
}
