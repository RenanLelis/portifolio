package security

import (
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"

	jwt "github.com/dgrijalva/jwt-go"
	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/config"
)

const AUTH string = "AUTH"

const CLAIMS_ID_USER string = "USER"
const CLAIMS_EMAIL string = "EMAIL"
const CLAIMS_ID_STATUS string = "STATUS"
const CLAIMS_AUTHORIZED string = "AUTHORIZED"
const CLAIMS_EXP_TIME string = "exp"

// const EXP_TIME time.Duration = time.Hour * 2
const EXP_TIME time.Duration = 2 * 60 * 60 * 1000

// CreateToken creates a JWT Token for the user
func CreateToken(userID uint64, userStatus uint64, userEmail string) (string, error) {
	claims := jwt.MapClaims{}
	claims[CLAIMS_AUTHORIZED] = true
	claims[CLAIMS_EXP_TIME] = time.Now().Add(EXP_TIME).Unix()
	claims[CLAIMS_ID_USER] = userID
	claims[CLAIMS_EMAIL] = userEmail
	claims[CLAIMS_ID_STATUS] = userStatus
	jwtToken := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return jwtToken.SignedString([]byte(config.SecretKey))
}

// ValidateToken check if the token on the request is a valid token and not expired
func ValidateToken(r *http.Request) error {
	tokenString := extractToken(r)
	token, err := jwt.Parse(tokenString, getValidationKey)
	if err != nil {
		return err
	}
	if _, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		return nil
	}
	return errors.New(messages.GetErrorMessageToken())
}

// RefreshToken, recriate the token with the old token
func RefreshToken(r *http.Request) (string, string, error) {
	userID, status, email, err := GetUserIDStatusAndEmail(r)
	if err != nil {
		return "", "", err
	}
	tokenString, err := CreateToken(userID, status, email)
	if err != nil {
		fmt.Println(err)
		return "", "", errors.New(messages.GetErrorMessageToken())
	}
	return AUTH, tokenString, nil
}

// GetUserID retorna o usuarioId que está salvo no token
func GetUserID(r *http.Request) (uint64, error) {
	tokenString := extractToken(r)
	token, err := jwt.Parse(tokenString, getValidationKey)
	if err != nil {
		return 0, err
	}
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		userID, err := strconv.ParseUint(fmt.Sprintf("%.0f", claims[CLAIMS_ID_USER]), 10, 64)
		if err != nil {
			return 0, err
		}
		return userID, nil
	}
	return 0, errors.New(messages.GetErrorMessageToken())
}

// GetUserIDStatusAndEmail gets the userID, status and email from the jwt token
func GetUserIDStatusAndEmail(r *http.Request) (uint64, uint64, string, error) {
	tokenString := extractToken(r)
	token, err := jwt.Parse(tokenString, getValidationKey)
	if err != nil {
		return 0, 0, "", err
	}
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		userID, erro := strconv.ParseUint(fmt.Sprintf("%.0f", claims[CLAIMS_ID_USER]), 10, 64)
		status, erro := strconv.ParseUint(fmt.Sprintf("%.0f", claims[CLAIMS_ID_STATUS]), 10, 64)
		email := fmt.Sprint(claims[CLAIMS_EMAIL])
		if erro != nil || userID == 0 {
			return 0, 0, "", erro
		}
		return userID, status, email, nil
	}
	return 0, 0, "", errors.New(messages.GetErrorMessageToken())
}

// extractToken gets the token from the request fi exsists, else return empty string
func extractToken(r *http.Request) string {
	token := r.Header.Get(AUTH)
	if len(strings.Split(token, " ")) == 2 {
		return strings.Split(token, " ")[1]
	}
	return ""
}

// getValidationKey returns the key used to sign the token
func getValidationKey(token *jwt.Token) (interface{}, error) {
	if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
		return nil, fmt.Errorf("Sign method unexpected! %v", token.Header["alg"])
	}
	return config.SecretKey, nil
}
