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
