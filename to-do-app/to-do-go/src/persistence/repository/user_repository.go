package repository

import (
	"database/sql"
	"errors"
	"strings"

	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"
	"renanlelis.github.io/portfolio/to-do-go/src/model"
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
	FROM USER_TODO WHERE EMAIL = ?`, email)
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
		return model.User{}, nil
	}
	return user, nil
}

// ActivateUser Activate a user on the database
func (repo UserRepository) ActivateUser(email string, activationCode string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET ACTIVATION_CODE = null, USER_STATUS = ? WHERE EMAIL = ? AND ACTIVATION_CODE = ?",
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
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET USER_PASSWORD = ?, NEW_PASSWORD_CODE = null, USER_STATUS = 1, ACTIVATION_CODE = null WHERE EMAIL = ? AND NEW_PASSWORD_CODE = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(hashPassword, email, newPasswordCode); err != nil {
		return err
	}
	return nil
}

// UpdateUserNewPasswordCode set a new password code for the user to reset his password
func (repo UserRepository) UpdateUserNewPasswordCode(email string, newPasswordCode string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET NEW_PASSWORD_CODE = ? WHERE EMAIL = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(newPasswordCode, email); err != nil {
		return err
	}
	return nil
}

// CreateNewUser create a new user on the database
func (repo UserRepository) CreateNewUser(email, password, firstName, lastName, activationCode string) (uint64, error) {
	statement, err := repo.db.Prepare(
		`INSERT INTO USER_TODO (EMAIL, USER_PASSWORD, FIRST_NAME, LAST_NAME, ACTIVATION_CODE, USER_STATUS)
		 VALUES (?, ?, ?, ?, ?, ?)`,
	)
	if err != nil {
		return 0, err
	}
	defer statement.Close()
	result, err := statement.Exec(email, password, firstName, lastName, activationCode, model.STATUS_INACTIVE)
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

// UpdateUserActivationCode update the activation code for the user
func (repo UserRepository) UpdateUserActivationCode(email, activationCode string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET ACTIVATION_CODE = ? WHERE EMAIL = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(activationCode, email); err != nil {
		return err
	}
	return nil
}

// UpdatePasswordById update user password on database by the user id
func (repo UserRepository) UpdatePasswordById(id uint64, hashPassword string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET USER_PASSWORD = ? WHERE ID = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(hashPassword, id); err != nil {
		return err
	}
	return nil
}

// UpdateUser update user info (first name and last name) on the database
func (repo UserRepository) UpdateUser(id uint64, firstName, lastName string) error {
	statement, err := repo.db.Prepare(
		"UPDATE USER_TODO SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?",
	)
	if err != nil {
		return err
	}
	defer statement.Close()

	if _, err = statement.Exec(firstName, lastName, id); err != nil {
		return err
	}
	return nil
}
