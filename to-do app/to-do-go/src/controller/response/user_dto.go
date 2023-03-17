package response

type UserDTO struct {
	Email     string `json:"email"`
	JWT       string `json:"jwt"`
	ID        uint64 `json:"id"`
	Status    uint64 `json:"status"`
	ExpiresIn uint64 `json:"expiresIn"`
	Name      string `json:"name"`
	LastName  string `json:"lastName"`
}

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
