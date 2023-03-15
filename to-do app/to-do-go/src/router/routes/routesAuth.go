package routes

import "net/http"

var authRoutes = []Route{
	{
		URI:    "/api/auth/login",
		Method: http.MethodPost,
		// Function:             controller.Login,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: false,
	},
	{
		URI:    "/api/auth/recoverpassword",
		Method: http.MethodPost,
		// Function:             controller.RecoverPassword,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: false,
	},
	{
		URI:    "/api/auth/passwordreset",
		Method: http.MethodPost,
		// Function:             controller.ResetPassword,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: false,
	},
	{
		URI:    "/api/auth/userregistration",
		Method: http.MethodPost,
		// Function:             controller.RegisterNewUser,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: false,
	},
	{
		URI:    "/api/auth/useractivation",
		Method: http.MethodPost,
		// Function:             controller.ActivateUser,
		Function:  func(w http.ResponseWriter, r *http.Request) {},
		NeedsAuth: false,
	},
}
