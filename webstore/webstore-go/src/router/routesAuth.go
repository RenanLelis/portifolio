package router

import (
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller"
)

func ConfigAuthRoutes(r *gin.Engine) {
	r.POST("/api/auth/login", controller.Login)
	r.POST("/api/auth/recoverpassword", controller.RecoverPassword)
	r.POST("/api/auth/passwordreset", controller.ResetPassword)
	r.POST("/api/auth/userregistration", controller.RegisterNewUser)
	r.POST("/api/auth/useractivation", controller.ActivateUser)
	r.POST("/api/auth/useractivationrequest", controller.RequestUserActivation)
}

// var authRoutes = []AppRoute{
// 	{
// 		URI:            "/api/auth/login",
// 		Method:         http.MethodPost,
// 		Function:       controller.Login,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// 	{
// 		URI:            "/api/auth/recoverpassword",
// 		Method:         http.MethodPost,
// 		Function:       controller.RecoverPassword,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// 	{
// 		URI:            "/api/auth/passwordreset",
// 		Method:         http.MethodPost,
// 		Function:       controller.ResetPassword,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// 	{
// 		URI:            "/api/auth/userregistration",
// 		Method:         http.MethodPost,
// 		Function:       controller.RegisterNewUser,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// 	{
// 		URI:            "/api/auth/useractivation",
// 		Method:         http.MethodPost,
// 		Function:       controller.ActivateUser,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// 	{
// 		URI:            "/api/auth/useractivationrequest",
// 		Method:         http.MethodPost,
// 		Function:       controller.RequestUserActivation,
// 		NeedsAdmin:     false,
// 		NeedsAuth:      false,
// 	},
// }
