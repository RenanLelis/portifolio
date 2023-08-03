package business

import (
	"renanlelis.github.io/portfolio/webstore-go/src/controller/dto"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/form"
)

// Login check user email and password to perform autentication
func Login(form form.LoginForm) (dto.UserDTO, Err) {
	//TODO
	return dto.UserDTO{}, Err{}
}

// RecoverPassword Generate a new Password Code and send an email with it
func RecoverPassword(form form.RecoverPasswordForm) Err {
	//TODO
	return Err{}
}

// ResetPassword reset user password by the code and authenticate the user
func ResetPassword(form form.ResetPasswordForm) (dto.UserDTO, Err) {
	//TODO
	return dto.UserDTO{}, Err{}
}

// RegisterNewUser creates a new user on the database
func RegisterNewUser(form form.NewUserForm) Err {
	//TODO
	return Err{}
}

// ActivateUser activate a user by the code sent by email
func ActivateUser(form form.UserActivationForm) (dto.UserDTO, Err) {
	//TODO
	return dto.UserDTO{}, Err{}
}

// RequestUserActivation send an email with a new activation code
func RequestUserActivation(form form.UserActivationRequestForm) Err {
	//TODO
	return Err{}
}
