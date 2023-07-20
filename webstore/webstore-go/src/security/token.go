package security

import (
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"strings"
	"time"

	jwt "github.com/golang-jwt/jwt"
	"renanlelis.github.io/portfolio/webstore-go/src/business/messages"
	"renanlelis.github.io/portfolio/webstore-go/src/config"
)

const AUTH string = "AUTHORIZATION"

const CLAIMS_ID_USER string = "USER_ID"
const CLAIMS_FIRST_NAME string = "FIRST_NAME"
const CLAIMS_LAST_NAME string = "LAST_NAME"
const CLAIMS_EMAIL string = "EMAIL"
const CLAIMS_ID_STATUS string = "STATUS"
const CLAIMS_ROLE string = "ROLE"
const CLAIMS_AUTHORIZED string = "AUTHORIZED"
const CLAIMS_EXP_TIME string = "exp"
const EXP_TIME int64 = 2 * 60 * 60 * 1000

// CreateToken creates a JWT Token for the user
func CreateToken(userID uint64, userStatus uint64, userEmail, firstName, lastName, role string) (string, error) {
	claims := jwt.MapClaims{}
	claims[CLAIMS_AUTHORIZED] = true
	claims[CLAIMS_EXP_TIME] = time.Now().Unix() + EXP_TIME
	claims[CLAIMS_ID_USER] = userID
	claims[CLAIMS_EMAIL] = userEmail
	claims[CLAIMS_FIRST_NAME] = firstName
	claims[CLAIMS_LAST_NAME] = lastName
	claims[CLAIMS_ID_STATUS] = userStatus
	claims[CLAIMS_ROLE] = role
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
func RefreshToken(jwtToken string) (string, error) {
	userID, status, email, firstName, lastName, role, err := GetUserDataFromToken(jwtToken)
	if err != nil {
		return "", err
	}
	tokenString, err := CreateToken(userID, status, email, firstName, lastName, role)
	if err != nil {
		return "", errors.New(messages.GetErrorMessageToken())
	}
	return tokenString, nil
}

// RefreshTokenFromRequest, recriate the token with the old token, receiving the request object
func RefreshTokenFromRequest(r *http.Request) (string, error) {
	userID, status, email, firstName, lastName, role, err := GetUserData(r)
	if err != nil {
		return "", err
	}
	tokenString, err := CreateToken(userID, status, email, firstName, lastName, role)
	if err != nil {
		return "", errors.New(messages.GetErrorMessageToken())
	}
	return tokenString, nil
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

// GetUserDataFromToken gets the userID, status, email, name and last name from the jwt token
func GetUserDataFromToken(tokenString string) (uint64, uint64, string, string, string, string, error) {
	token, err := jwt.Parse(tokenString, getValidationKey)
	if err != nil {
		return 0, 0, "", "", "", "", err
	}
	if claims, ok := token.Claims.(jwt.MapClaims); ok && token.Valid {
		userID, err := strconv.ParseUint(fmt.Sprintf("%.0f", claims[CLAIMS_ID_USER]), 10, 64)
		status, err := strconv.ParseUint(fmt.Sprintf("%.0f", claims[CLAIMS_ID_STATUS]), 10, 64)
		email := fmt.Sprint(claims[CLAIMS_EMAIL])
		firstName := fmt.Sprint(claims[CLAIMS_FIRST_NAME])
		lastName := fmt.Sprint(claims[CLAIMS_LAST_NAME])
		role := fmt.Sprint(claims[CLAIMS_ROLE])
		if err != nil || userID == 0 {
			return 0, 0, "", "", "", "", err
		}
		return userID, status, email, firstName, lastName, role, nil
	}
	return 0, 0, "", "", "", "", errors.New(messages.GetErrorMessageToken())
}

// GetUserData gets the userID, status, email, name and last name from the jwt token on the request
func GetUserData(r *http.Request) (uint64, uint64, string, string, string, string, error) {
	tokenString := extractToken(r)
	return GetUserDataFromToken(tokenString)
}

// extractToken gets the token from the request fi exsists, else return empty string
func extractToken(r *http.Request) string {
	token := r.Header.Get(AUTH)
	if len(token) > 0 && strings.HasPrefix(token, "Bearer") {
		newToken := token[7:]
		return newToken
	}
	return token
}

// getValidationKey returns the key used to sign the token
func getValidationKey(token *jwt.Token) (interface{}, error) {
	if _, ok := token.Method.(*jwt.SigningMethodHMAC); !ok {
		return nil, fmt.Errorf("Sign method unexpected! %v", token.Header["alg"])
	}
	return config.SecretKey, nil
}
