package form

// UserProfileForm form data for update user profile
type UserProfileForm struct {
	FirstName string `json:"firstName"`
	LastName  string `json:"lastName,omitempty"`
}

// UserPasswordForm form data for update user password
type UserPasswordForm struct {
	Password string `json:"password"`
}
