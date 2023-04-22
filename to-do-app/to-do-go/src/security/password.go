package security

import (
	"errors"
	"math/rand"
	"strings"

	"renanlelis.github.io/portfolio/to-do-go/src/business/messages"

	"golang.org/x/crypto/bcrypt"
)

// Hash generate a hash for a string
func Hash(senha string) ([]byte, error) {
	return bcrypt.GenerateFromPassword([]byte(senha), bcrypt.DefaultCost)
}

// HashString generate a hash for a string and return as a string
func HashString(senha string) (string, error) {
	senhaHash, erro := bcrypt.GenerateFromPassword([]byte(senha), bcrypt.DefaultCost)
	if erro != nil {
		return "", erro
	}
	return string(senhaHash), nil
}

// ComparePassword compares a hash password and a string password to check if they are equal
func ComparePassword(hashPassword, stringPassword string) error {
	return bcrypt.CompareHashAndPassword([]byte(hashPassword), []byte(stringPassword))
}

// ValidatePasswordParameters validate the password according to some parameters such as length
func ValidatePasswordParameters(password string) error {
	formatedPassword := strings.TrimSpace(password)
	if len(formatedPassword) < 6 || len(formatedPassword) > 16 {
		return errors.New(messages.GetErrorMessageInputValues())
	}
	return nil
}

// GenerateRandoStringCode generates a new random code for password reset or user activation
func GenerateRandoStringCode() string {
	chars := []rune("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")
	length := 6
	var b strings.Builder
	for i := 0; i < length; i++ {
		b.WriteRune(chars[rand.Intn(len(chars))])
	}
	str := b.String()
	return str
}
