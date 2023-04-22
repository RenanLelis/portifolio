package messages

const MSE01 string = "MSE01" // An error ocurred on the operation.
const MSE02 string = "MSE02" // Error on values properties.
const MSE03 string = "MSE03" // Invalid Token.
const MSE04 string = "MSE04" // Invalid User or Password.
const MSE05 string = "MSE05" // E-mail already exists.
const MSE06 string = "MSE06" // E-mail not found.
const MSE07 string = "MSE07" // User not activated.
const MSE08 string = "MSE08" // Cannot delete only list for the user.

// GetErrorMessage return a generic error message
func GetErrorMessage() string {
	return MSE01
}

// GetErrorMessageInputValues return a error message about the input values
func GetErrorMessageInputValues() string {
	return MSE02
}

// GetErrorMessageToken return an error message about the invalid jwt token
func GetErrorMessageToken() string {
	return MSE03
}

// GetErrorMessageLogin return an error message about invalid user or password
func GetErrorMessageLogin() string {
	return MSE04
}

// GetErrorMessageEmailAlreadyExists return an error message about a email that already exists
func GetErrorMessageEmailAlreadyExists() string {
	return MSE05
}

// GetErrorMessageUserNotFound return an error message about an email not found on database
func GetErrorMessageUserNotFound() string {
	return MSE06
}

// GetErrorMessageUserNotFound return an error message about user not active on the system
func GetErrorMessageUserNotActive() string {
	return MSE07
}

// GetErrorMessageCannotDeleteOnlyList return an error message about deletion of user's only list
func GetErrorMessageCannotDeleteOnlyList() string {
	return MSE08
}
