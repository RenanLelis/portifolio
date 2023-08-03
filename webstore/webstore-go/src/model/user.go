package model

import "go.mongodb.org/mongo-driver/bson/primitive"

const STATUS_ACTIVE uint64 = 1
const STATUS_INACTIVE uint64 = 0

const ROLE_PUBLIC string = "PUBLIC"
const ROLE_ADMIN string = "ADMIN"

// User represents the User Table on the database
type User struct {
	ID             primitive.ObjectID `bson:"_id"`
	Email          string `bson:"email"`
	Password       string `bson:"password"`
	ActivationCode string `bson:"activation_code"`
	FirstName      string `bson:"first_name"`
	LastName       string `bson:"last_name"`
	Status         uint64 `bson:"status"`
	Roles          []string `bson:"roles"`
}