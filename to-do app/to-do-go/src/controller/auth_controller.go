package controller

import (
	"encoding/json"
	"io/ioutil"
	"net/http"

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
	//TODO
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
	//TODO
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
	//TODO
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
	//TODO
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
	//TODO
}
