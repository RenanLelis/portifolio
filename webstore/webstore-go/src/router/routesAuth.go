package router

import (
	"net/http"

	"renanlelis.github.io/portfolio/webstore-go/src/controller"
)

var authRoutes = []AppRoute{
	{
		URI:            "/api/auth/login",
		Method:         http.MethodPost,
		Function:       controller.Login,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
	{
		URI:            "/api/auth/recoverpassword",
		Method:         http.MethodPost,
		Function:       controller.RecoverPassword,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
	{
		URI:            "/api/auth/passwordreset",
		Method:         http.MethodPost,
		Function:       controller.ResetPassword,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
	{
		URI:            "/api/auth/userregistration",
		Method:         http.MethodPost,
		Function:       controller.RegisterNewUser,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
	{
		URI:            "/api/auth/useractivation",
		Method:         http.MethodPost,
		Function:       controller.ActivateUser,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
	{
		URI:            "/api/auth/useractivationrequest",
		Method:         http.MethodPost,
		Function:       controller.RequestUserActivation,
		NeedsAdmin:     false,
		NeedsAuth:      false,
		NeedsRecaptcha: true,
	},
}
