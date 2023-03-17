package controller

import (
	"encoding/json"
	"io/ioutil"
	"net/http"

	"renan.com/todo/src/controller/response"
)

// Login implements the controller for login requests
func Login(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err)
		return
	}

	var form response.LoginForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err)
		return
	}
	//TODO
}

// RecoverPassword implements the controller for the forget password operation
func RecoverPassword(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err)
		return
	}

	var form response.RecoverPasswordForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err)
		return
	}
	//TODO
}

// ResetPassword implements the controller to reset password with a code
func ResetPassword(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err)
		return
	}

	var form response.ResetPasswordForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err)
		return
	}
	//TODO
}

// RegisterNewUser implements the controller to new user registration
func RegisterNewUser(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err)
		return
	}

	var form response.NewUserForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err)
		return
	}
	//TODO
}

// ActivateUser implements the controller for user activation
func ActivateUser(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err)
		return
	}

	var form response.UserActivationForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err)
		return
	}
	//TODO
}
