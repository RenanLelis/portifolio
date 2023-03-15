package repository

import (
	"database/sql"
	"errors"
	"strings"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/model"
)

// UserRepository represents a user repository for database connection
type UserRepository struct {
	db *sql.DB
}

// CreateUserRepository create a new user repository
func CreateUserRepository(db *sql.DB) *UserRepository {
	return &UserRepository{db}
}

// GetUserByEmail fetch a user from the database by the email
func (repo UserRepository) GetUserByEmail(email string) (model.User, error) {
	queryResult, err := repo.db.Query(`SELECT 
	ID, EMAIL, USER_PASSWORD, FIRST_NAME, LAST_NAME, USER_STATUS 
	FROM USER WHERE EMAIL = ?`, email)
	if err != nil {
		return model.User{}, errors.New(messages.GetErrorMessageUserNotFound())
	}
	defer queryResult.Close()

	var user model.User
	if queryResult.Next() {
		if err = queryResult.Scan(
			&user.ID,
			&user.Email,
			&user.Password,
			&user.FirstName,
			&user.LastName,
			&user.Status,
		); err != nil {
			return model.User{}, err
		}
	} else {
		return model.User{}, errors.New(messages.GetErrorMessageUserNotFound())
	}
	return user, nil
}

// ActivateUser Activate a user on the database
func (repo UserRepository) ActivateUser(email string, activationCode string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER SET ACTIVATION_CODE = null, USER_STATUS = ? WHERE EMAIL = ? AND ACTIVATION_CODE = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(model.STATUS_ACTIVE, email, activationCode); err != nil {
		return err
	}
	return nil
}

// UpdatePasswordByCode update user password on database by the code sent
func (repo UserRepository) UpdatePasswordByCode(email string, newPasswordCode string, hashPassword string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE USER SET USER_PASSWORD = ?, NEW_PASSWORD_CODE = null WHERE EMAIL = ? AND NEW_PASSWORD_CODE = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(hashPassword, email, newPasswordCode); erro != nil {
		return erro
	}
	return nil
}

// UpdateUserNewPasswordCode set a new password code for the user to reset his password
func (repo UserRepository) UpdateUserNewPasswordCode(email string, newPasswordCode string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE USER SET NEW_PASSWORD_CODE = ? WHERE EMAIL = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(newPasswordCode, email); erro != nil {
		return erro
	}
	return nil
}

// CreateNewUser create a new user on the database
func (repo UserRepository) CreateNewUser(email, password, firstName, lastName, activationCode string) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO USER (EMAIL, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVATION_CODE, USER_STATUS)
		 VALUES (?, ?, ?, ?, ?, ?)`,
	)
	if err != nil {
		return 0, err
	}
	defer statement.Close()
	result, err := statement.Exec(email, password, firstName, lastName, activationCode, model.STATUS_ACTIVE)
	if err != nil {
		if strings.Contains(err.Error(), "Duplicate entry") {
			return 0, errors.New(messages.GetErrorMessageEmailAlreadyExists())
		}
		return 0, errors.New(messages.GetErrorMessage())
	}
	newID, err := result.LastInsertId()
	if err != nil {
		return 0, err
	}
	return uint64(newID), nil
}

// UpdatePasswordById update user password on database by the user id
func (repo UserRepository) UpdatePasswordById(id uint64, hashPassword string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE USER SET USER_PASSWORD = ? WHERE ID = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(hashPassword, id); erro != nil {
		return erro
	}
	return nil
}

// UpdateUser update user info (first name and last name) on the database
func (repo UserRepository) UpdateUser(id uint64, firstName, lastName string) error {
	statement, erro := repo.db.Prepare(
		"UPDATE USER SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?",
	)
	if erro != nil {
		return erro
	}
	defer statement.Close()

	if _, erro = statement.Exec(firstName, lastName, id); erro != nil {
		return erro
	}
	return nil
}
