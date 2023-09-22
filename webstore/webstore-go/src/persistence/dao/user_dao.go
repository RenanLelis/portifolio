package dao

import (
	"context"

	"renanlelis.github.io/portfolio/webstore-go/src/database"
	"renanlelis.github.io/portfolio/webstore-go/src/model"
	"renanlelis.github.io/portfolio/webstore-go/src/persistence/repository"
)

// GetUserByEmail fetch a user from the database by the email
func GetUserByEmail(email string) (model.User, error) {
	db, err := database.ConnectToDataBase()
	if err != nil {
		return model.User{}, err
	}
	defer db.Disconnect(context.TODO())
	rep := repository.CreateUserRepository(db)
	return rep.GetUserByEmail(email)
}