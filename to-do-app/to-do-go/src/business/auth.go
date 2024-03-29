package business

import (
	"errors"
	"net/http"
	"strings"

	"github.com/badoux/checkmail"
	"renanlelis.github.io/portfolio/to-do-go/src/business/mail"
	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/controller/response"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
	"renanlelis.github.io/portfolio/to-do-go/src/persistence/dao"
	"renanlelis.github.io/portfolio/to-do-go/src/security"
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
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageLogin()}
	}
	if err = security.ComparePassword(user.Password, passwordFormatted); err != nil {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageLogin()}
	}
	if user.Status == model.STATUS_INACTIVE {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageUserNotActive()}
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
		if err.Error() == messages.GetErrorMessageUserNotFound() {
			status = http.StatusBadRequest
		}
		return Err{ErrorMessage: err.Error(), Status: status}
	}
	err = mail.SendEmailRecoverPassword(email, newPasswordCode)
	if err != nil {
		return Err{ErrorMessage: messages.GetErrorMessage(), Status: http.StatusInternalServerError}
	}
	return Err{}
}

// ResetPassword update user password by code sent by emaul
func ResetPassword(email, password, newPasswordCode string) (response.UserDTO, Err) {
	emailFormatted := strings.TrimSpace(email)
	if !isEmailValid(emailFormatted) || password == "" || len(password) < 6 || newPasswordCode == "" || len(newPasswordCode) != 6 {
		return response.UserDTO{}, Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	user, err := dao.GetUserByEmail(email)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if user.ID <= 0 {
		return response.UserDTO{}, Err{http.StatusForbidden, messages.GetErrorMessageUserNotFound()}
	}
	hashPassword, err := security.HashString(password)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	err = dao.UpdatePasswordByCode(email, newPasswordCode, hashPassword)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	user.Status = model.STATUS_ACTIVE
	userDTO, err := response.ConvertUserToUserDTO(user)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, err.Error()}
	}
	return userDTO, Err{}
}

// RegisterNewUser creates a new user on the database
func RegisterNewUser(email, password, firstName, lastName string) Err {
	emailFormatted := strings.TrimSpace(email)
	passwordFormatted := strings.TrimSpace(password)
	firstNameFormatted := strings.TrimSpace(firstName)
	lastNameFormatted := strings.TrimSpace(lastName)
	err := validateNewUserData(emailFormatted, passwordFormatted, firstNameFormatted, lastNameFormatted)
	if err != nil {
		return Err{http.StatusBadRequest, err.Error()}
	}
	user, err := dao.GetUserByEmail(emailFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if user.ID > 0 {
		return Err{http.StatusForbidden, messages.GetErrorMessageEmailAlreadyExists()}
	}
	activationCode := security.GenerateRandoStringCode()
	hashPass, err := security.HashString(passwordFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	userID, err := dao.CreateNewUser(emailFormatted, hashPass, firstNameFormatted, lastNameFormatted, activationCode)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	list := CreateDefaultList(userID)
	_, err = dao.CreateNewList(list.ListName, list.ListDescription, userID)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	err = mail.SendEmailUserActivation(emailFormatted, activationCode)
	if err != nil {
		return Err{ErrorMessage: messages.GetErrorMessage(), Status: http.StatusInternalServerError}
	}
	return Err{}
}

// ActivateUser set status active for a user, validating email and code sent by email
func ActivateUser(email, activationCode string) (response.UserDTO, Err) {
	emailFormatted := strings.TrimSpace(email)
	activationCodeFormatted := strings.TrimSpace(activationCode)
	err := validateUserActivationData(emailFormatted, activationCodeFormatted)
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
	err = dao.ActivateUser(emailFormatted, activationCodeFormatted)
	if err != nil {
		return response.UserDTO{}, Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	user.ActivationCode = ""
	user.Status = model.STATUS_ACTIVE
	userDTO, err := response.ConvertUserToUserDTO(user)
	if err != nil {
		return response.UserDTO{}, Err{ErrorMessage: messages.GetErrorMessage(), Status: http.StatusInternalServerError}
	}
	return userDTO, Err{}
}

// RequestUserActivation set a new activation code and send by email for the user
func RequestUserActivation(email string) Err {
	emailFormatted := strings.TrimSpace(email)
	if !isEmailValid(emailFormatted) {
		return Err{http.StatusBadRequest, messages.GetErrorMessageInputValues()}
	}
	user, err := dao.GetUserByEmail(emailFormatted)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	if user.ID <= 0 {
		return Err{http.StatusForbidden, messages.GetErrorMessageUserNotFound()}
	}
	activationCode := security.GenerateRandoStringCode()
	err = dao.UpdateUserActivationCode(emailFormatted, activationCode)
	if err != nil {
		return Err{http.StatusInternalServerError, messages.GetErrorMessage()}
	}
	err = mail.SendEmailUserActivation(email, activationCode)
	if err != nil {
		return Err{ErrorMessage: messages.GetErrorMessage(), Status: http.StatusInternalServerError}
	}
	return Err{}
}

// validateLoginData validate email and password informed to login operation
func validateLoginData(email, password string) error {
	if !isEmailValid(email) || password == "" || len(password) < 6 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// validateNewUserData validate email, password, firs name and last name informed to user registration operation
func validateNewUserData(email, password, firstName, lastName string) error {
	if !isEmailValid(email) || password == "" || len(password) < 6 || firstName == "" || len(firstName) <= 0 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// validateUserActivationData validate email, and activation code informed to user activation operation
func validateUserActivationData(email, activationCode string) error {
	if !isEmailValid(email) || activationCode == "" || len(activationCode) != 6 {
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
