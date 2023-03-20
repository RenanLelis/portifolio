package model

const STATUS_ACTIVE uint64 = 1
const STATUS_INACTIVE uint64 = 0

// User represents the User Table on the database
type User struct {
	ID             uint64
	Email          string
	Password       string
	ActivationCode string
	FirstName      string
	LastName       string
	Status         uint64
}
