package repository

import (
	"context"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo"
	"renanlelis.github.io/portfolio/webstore-go/src/database"
	"renanlelis.github.io/portfolio/webstore-go/src/model"
)

type UserRepository struct {
	db *mongo.Client
}

// CreateUserRepository create a new user repository
func CreateUserRepository(db *mongo.Client) *UserRepository {
	return &UserRepository{db}
}

// GetUserByEmail fetch a user from the database by the email
func (repo UserRepository) GetUserByEmail(email string) (model.User, error) {
	collection := database.GetCollection(repo.db,"app_user")
	var user model.User
	err := collection.FindOne(context.TODO(), bson.D{{"email", email}}).Decode(&user)
	if err != nil {
		return model.User{}, err
	}
	return user, nil
}