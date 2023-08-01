package controller

import (
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/form"
)

// Login implements the controller for login requests
func Login(ctx *gin.Context) {
	var form form.LoginForm
	ctx.BindJSON(&form)
	//TODO
}

// RecoverPassword implements the controller for the forget password operation
func RecoverPassword(ctx *gin.Context) {
	//TODO
}

// ResetPassword implements the controller to reset password with a code
func ResetPassword(ctx *gin.Context) {
	//TODO
}

// RegisterNewUser implements the controller to new user registration
func RegisterNewUser(ctx *gin.Context) {
	//TODO
}

// ActivateUser implements the controller for user activation
func ActivateUser(ctx *gin.Context) {
	//TODO
}

// RequestUserActivation request a activation for a user that didn't do it on the registration
func RequestUserActivation(ctx *gin.Context) {
	//TODO
}
