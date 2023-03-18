package response

import (
	"errors"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/model"
	"renan.com/todo/src/security"
)

// ConvertUserToUserDTO create a DTO with the user as param
func ConvertUserToUserDTO(user model.User) (UserDTO, error) {
	jwt, err := security.CreateToken(user.ID, user.Status, user.Email)
	if err != nil {
		return UserDTO{}, errors.New(messages.GetErrorMessageToken())
	}
	return UserDTO{
		ID:        user.ID,
		Email:     user.Email,
		Status:    user.Status,
		Name:      user.FirstName,
		LastName:  user.LastName,
		ExpiresIn: uint64(security.EXP_TIME),
		JWT:       jwt,
	}, nil
}
