package controller

import (
	"errors"
	"net/http"

	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/business"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/form"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/response"
)

// Login implements the controller for login requests
func Login(ctx *gin.Context) {
	var form form.LoginForm
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	userDTO, bErr := business.Login(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, userDTO)
}

// RecoverPassword implements the controller for the forget password operation
func RecoverPassword(ctx *gin.Context) {
	var form form.RecoverPasswordForm
	ctx.BindJSON(&form)
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	bErr := business.RecoverPassword(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, nil)
}

// ResetPassword implements the controller to reset password with a code
func ResetPassword(ctx *gin.Context) {
	var form form.ResetPasswordForm
	ctx.BindJSON(&form)
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	userDTO, bErr := business.ResetPassword(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, userDTO)
}

// RegisterNewUser implements the controller to new user registration
func RegisterNewUser(ctx *gin.Context) {
	var form form.NewUserForm
	ctx.BindJSON(&form)
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	bErr := business.RegisterNewUser(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, nil)
}

// ActivateUser implements the controller for user activation
func ActivateUser(ctx *gin.Context) {
	var form form.UserActivationForm
	ctx.BindJSON(&form)
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	userDTO, bErr := business.ActivateUser(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, userDTO)
}

// RequestUserActivation request a activation for a user that didn't do it on the registration
func RequestUserActivation(ctx *gin.Context) {
	var form form.UserActivationRequestForm
	ctx.BindJSON(&form)
	err := ctx.BindJSON(&form)
	if err != nil {
		response.Err(ctx, http.StatusBadRequest, err)
		return
	}
	bErr := business.RequestUserActivation(form)
	if bErr.Status > 0 {
		response.Err(ctx, bErr.Status, errors.New(bErr.ErrorMessage))
		return
	}
	response.JSON(ctx, http.StatusOK, nil)
}
