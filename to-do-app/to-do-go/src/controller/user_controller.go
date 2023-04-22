package controller

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"net/http"

	"renanlelis.github.io/portfolio/to-do-go/src/business"
	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/controller/form"
	"renanlelis.github.io/portfolio/to-do-go/src/controller/response"
	"renanlelis.github.io/portfolio/to-do-go/src/security"
)

// UpdateUserProfile update user information like first name or last name
func UpdateUserProfile(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusInternalServerError, errors.New(messages.GetErrorMessage()), r)
		return
	}
	var form form.UserProfileForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}
	bErr := business.UpdateUserProfile(userID, form.FirstName, form.LastName)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}

// UpdateUserPassword update user password after user is logged in
func UpdateUserPassword(w http.ResponseWriter, r *http.Request) {
	reqBody, err := ioutil.ReadAll(r.Body)
	if err != nil {
		response.Err(w, http.StatusUnprocessableEntity, err, r)
		return
	}
	userID, err := security.GetUserID(r)
	if err != nil {
		response.Err(w, http.StatusInternalServerError, errors.New(messages.GetErrorMessage()), r)
		return
	}
	var form form.UserPasswordForm
	if err = json.Unmarshal(reqBody, &form); err != nil {
		response.Err(w, http.StatusBadRequest, err, r)
		return
	}

	bErr := business.UpdateUserPassword(userID, form.Password)
	if bErr.Status > 0 {
		response.Err(w, bErr.Status, errors.New(bErr.ErrorMessage), r)
		return
	}
	response.JSON(w, http.StatusOK, nil, r)
}
