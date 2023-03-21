package response

import "renan.com/todo/src/security"

// UserDTO struct with data to use as response for login operations
type UserDTO struct {
	Email     string `json:"email"`
	JWT       string `json:"jwt"`
	ID        uint64 `json:"id"`
	Status    uint64 `json:"status"`
	ExpiresIn uint64 `json:"expiresIn"`
	Name      string `json:"name"`
	LastName  string `json:"lastName"`
}

//CreateUserDTOFromJWT create a new DTO Object from the token
func CreateUserDTOFromJWT(jwtToken string) UserDTO {
	userID, status, email, firstName, lastName, err := security.GetUserDataFromToken(jwtToken)
	if err != nil {
		return UserDTO{}
	}
	newToken, err := security.CreateToken(userID, status, email, firstName, lastName)
	if err != nil {
		return UserDTO{}
	}
	return UserDTO{
		Email:     email,
		ID:        userID,
		Status:    status,
		Name:      firstName,
		LastName:  lastName,
		ExpiresIn: uint64(security.EXP_TIME),
		JWT:       newToken,
	}
}
