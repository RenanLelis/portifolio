package form

// LoginForm form data for login operations
type LoginForm struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

// RecoverPasswordForm form data to recover password
type RecoverPasswordForm struct {
	Email string `json:"email"`
}

// ResetPasswordForm form data to reset password with code
type ResetPasswordForm struct {
	Email           string `json:"email"`
	Password        string `json:"password"`
	NewPasswordCode string `json:"newPasswordCode"`
}

// NewUserForm form data for new user registration
type NewUserForm struct {
	Email     string `json:"email"`
	Password  string `json:"password"`
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName,omitempty"`
}

// UserActivationForm form data for user activation
type UserActivationForm struct {
	Email          string `json:"email"`
	ActivationCode string `json:"activationCode"`
}

//UserActivationRequestForm form data to request user activation
type UserActivationRequestForm struct {
	Email string `json:"email"`
}
