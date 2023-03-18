package dao

import (
	"errors"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/database"
	"renan.com/todo/src/model"
	"renan.com/todo/src/persistence/repository"
)

// GetUserByEmail fetch a user from the database by the email
func GetUserByEmail(email string) (model.User, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return model.User{}, err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.GetUserByEmail(email)
}

// ActivateUser Activate a user on the database
func ActivateUser(email string, activationCode string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.ActivateUser(email, activationCode)
}

// UpdatePasswordByCode update user password on database by the code sent
func UpdatePasswordByCode(email string, newPasswordCode string, hashPassword string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.UpdatePasswordByCode(email, newPasswordCode, hashPassword)
}

// UpdateUserNewPasswordCode set a new password code for the user to reset his password
func UpdateUserNewPasswordCode(email string, newPasswordCode string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	user, err := rep.GetUserByEmail(email)
	if err != nil {
		return errors.New(messages.GetErrorMessage())
	}
	if user.ID != 0 {
		return errors.New(messages.GetErrorMessageEmailAlreadyExists())
	}
	return rep.UpdateUserNewPasswordCode(email, newPasswordCode)
}

// CreateNewUser create a new user on the database
func CreateNewUser(email, password, firstName, lastName, activationCode string) (uint64, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return 0, err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.CreateNewUser(email, password, firstName, lastName, activationCode)
}

// UpdatePasswordById update user password on database by the user id
func UpdatePasswordById(id uint64, hashPassword string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.UpdatePasswordById(id, hashPassword)
}

// UpdateUser update user info (first name and last name) on the database
func UpdateUser(id uint64, firstName, lastName string) error {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return err
	}
	defer db.Close()
	rep := repository.CreateUserRepository(db)
	return rep.UpdateUser(id, firstName, lastName)
}
