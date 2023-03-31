package controller

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"net/http"

	"renan.com/todo/src/business"
	"renan.com/todo/src/controller/form"
	"renan.com/todo/src/controller/response"
)

// Login implements the controller for login requests
func Login(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.LoginForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userDTO, bErr := business.Login(form.Email, form.Password)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, userDTO, r)
}

// RecoverPassword implements the controller for the forget password operation
func RecoverPassword(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.RecoverPasswordForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.RecoverPassword(form.Email)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// ResetPassword implements the controller to reset password with a code
func ResetPassword(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	var form form.ResetPasswordForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userDTO, bErr := business.ResetPassword(form.Email, form.Password, form.NewPasswordCode)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, userDTO, r)
}

// RegisterNewUser implements the controller to new user registration
func RegisterNewUser(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}

	var form form.NewUserForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.RegisterNewUser(form.Email, form.Password, form.FirstName, form.LastName)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusCreated, nil, r)
}

// ActivateUser implements the controller for user activation
func ActivateUser(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}

	var form form.UserActivationForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	userDTO, bErr := business.ActivateUser(form.Email, form.ActivationCode)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, userDTO, r)
}

// RequestUserActivation request a activation for a user that didn't do it on the registration
func RequestUserActivation(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}

	var form form.UserActivationRequestForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}

	bErr := business.RequestUserActivation(form.Email)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}
