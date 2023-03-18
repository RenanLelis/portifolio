package business

import (
	"errors"
	"fmt"
	"net/http"
	"strings"

	"github.com/badoux/checkmail"
	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/controller/response"
	"renan.com/todo/src/persistence/dao"
	"renan.com/todo/src/security"
)

// Login check user email and password to perform autentication
func Login(email, password string) (response.UserDTO, Err) {
	emailFormatted := strings.TrimSpace(email)
	passwordFormatted := strings.TrimSpace(password)
	err := validateLoginData(emailFormatted, passwordFormatted)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusBadRequest, err.Error()}
	}
	user, err := dao.GetUserByEmail(emailFormatted)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if user.ID <= 0 {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageUserNotFound()}
	}
	if err = security.ComparePassword(user.Password, passwordFormatted); err != nil {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageUserNotFound()}
	}
	userDTO, err := response.ConvertUserToUserDTO(user)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, err.Error()}
	}
	return userDTO, Err{}
}

// RecoverPassword helps the user to recover password by a code sent by email
func RecoverPassword(email string) Err {
	emailFormatted := strings.TrimSpace(email)
	if !isEmailValid(emailFormatted) {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	newPasswordCode := security.GenerateRandoStringCode()
	err := dao.UpdateUserNewPasswordCode(emailFormatted, newPasswordCode)
	if err != nil {
		status := http.StatusInternalServerError
		if err.Error() == messages.GetErrorMessageEmailAlreadyExists() {
			status = http.StatusBadRequest
		}
		return Err{ErrorMessage: err.Error(), Status: status}
	}
	return Err{}
}

// ResetPassword update user password by code sent by emaul
func ResetPassword(email, password, newPasswordCode string) (response.UserDTO, Err) {
	emailFormatted := strings.TrimSpace(email)
	if !isEmailValid(emailFormatted) || password == "" || len(password) < 6 || newPasswordCode == "" || len(newPasswordCode) != 6 {
		return response.UserDTO{}, Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	hashPassword, err := security.HashString(password)
	if err != nil {
		fmt.Println(err)
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	err = dao.UpdatePasswordByCode(email, newPasswordCode, hashPassword)
	if err != nil {
		fmt.Println(err)
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	user, err := dao.GetUserByEmail(email)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if user.ID <= 0 {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageUserNotFound()}
	}
	userDTO, err := response.ConvertUserToUserDTO(user)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, err.Error()}
	}
	return userDTO, Err{}
}

// RegisterNewUser creates a new user on the database
func RegisterNewUser(email, password, firstName, lastName string) (response.UserDTO, Err) {
	//TODO
	return response.UserDTO{}, Err{}
}

// ActivateUser set status active for a user, validating email and code sent by email
func ActivateUser(email, activationCode string) (response.UserDTO, Err) {
	//TODO
	return response.UserDTO{}, Err{}
}

func validateLoginData(email, password string) error {
	if !isEmailValid(email) || password == "" || len(password) < 6 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// isEmailValid check if the email is valid
func isEmailValid(email string) bool {
	if err := checkmail.ValidateFormat(strings.TrimSpace(email)); err != nil {
		return false
	}
	return true
}
