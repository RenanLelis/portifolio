package business

import (
	"errors"
	"net/http"
	"strings"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/persistence/dao"
	"renan.com/todo/src/security"
)

// UpdateUserProfile update user information like first name or last name
func UpdateUserProfile(userID uint64, firstName, lastName string) Err {
	firstNameFormatted := strings.TrimSpace(firstName)
	lastNameFormatted := strings.TrimSpace(lastName)
	err := validateUserData(firstNameFormatted, lastNameFormatted, userID)
	if err != nil {
		return Err{http.StatusBadRequest, err.Error()}
	}
	err = dao.UpdateUser(userID, firstNameFormatted, lastNameFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// UpdateUserPassword update user password after user is logged in
func UpdateUserPassword(userID uint64, password string) Err {
	passwordFormatted := strings.TrimSpace(password)
	err := validateUserPassword(passwordFormatted, userID)
	if err != nil {
		return Err{http.StatusBadRequest, err.Error()}
	}
	hashPassword, err := security.HashString(passwordFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	err = dao.UpdatePasswordById(userID, hashPassword)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	return Err{}
}

// validateUserData validates user firstName and LastName for user update
func validateUserData(firstName, lastName string, userID uint64) error {
	if firstName == "" || len(firstName) <= 0 || userID <= 0 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// validateUserData validates user firstName and LastName for user update
func validateUserPassword(password string, userID uint64) error {
	if err := security.ValidatePasswordParameters(password); err != nil || userID <= 0 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}
