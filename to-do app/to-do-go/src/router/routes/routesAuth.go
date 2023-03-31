package routes

import (
	"net/http"

	"renan.com/todo/src/controller"
)

var authRoutes = []Route{
	{
		URI:       "/api/auth/login",
		Method:    http.MethodPost,
		Function:  controller.Login,
		NeedsAuth: false,
	},
	{
		URI:       "/api/auth/recoverpassword",
		Method:    http.MethodPost,
		Function:  controller.RecoverPassword,
		NeedsAuth: false,
	},
	{
		URI:       "/api/auth/passwordreset",
		Method:    http.MethodPost,
		Function:  controller.ResetPassword,
		NeedsAuth: false,
	},
	{
		URI:       "/api/auth/userregistration",
		Method:    http.MethodPost,
		Function:  controller.RegisterNewUser,
		NeedsAuth: false,
	},
	{
		URI:       "/api/auth/useractivation",
		Method:    http.MethodPost,
		Function:  controller.ActivateUser,
		NeedsAuth: false,
	},
	{
		URI:       "/api/auth/useractivationrequest",
		Method:    http.MethodPost,
		Function:  controller.RequestUserActivation,
		NeedsAuth: false,
	},
}
