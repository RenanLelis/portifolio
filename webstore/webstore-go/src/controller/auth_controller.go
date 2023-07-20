package controller

import (
	"encoding/json"
	"io/ioutil"

	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/form"
)

// Login implements the controller for login requests
func Login(ctx *gin.Context) {
	reqBody, err := ioutil.ReadAll(ctx.Request.Body)
	if err != nil {
		// response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.LoginForm
	if err := json.Unmarshal(reqBody, &form); err != nil {
		// response.Err(w, http.StatusBadRequest, err, r)
		return
	}
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
