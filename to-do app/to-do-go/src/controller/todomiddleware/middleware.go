package todomiddleware

import (
	"errors"
	"net/http"
	"strconv"

	"renan.com/todo/src/business/messages"
	"renan.com/todo/src/controller/response"
	"renan.com/todo/src/model"
	"renan.com/todo/src/security"
)

const HEADER_USER_ID string = "userID"
const HEADER_USER_STATUS string = "userStatus"
const HEADER_USER_EMAIL string = "userEmail"

// ValidateAuth check if there is a valid JWT Token for a user
func ValidateAuth(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		userID, status, email, _, _, err := security.GetUserData(r)
		if err != nil {
			response.Err(w, http.StatusUnauthorized, err, r)
			return
		}
		if userID == 0 || status == model.STATUS_INACTIVE {
			response.Err(w, http.StatusUnauthorized, errors.New(messages.GetErrorMessageToken()), r)
			return
		}
		r.Header.Add(HEADER_USER_ID, strconv.FormatUint(userID, 10))
		r.Header.Add(HEADER_USER_STATUS, strconv.FormatUint(status, 10))
		r.Header.Add(HEADER_USER_EMAIL, email)
		next(w, r)
	}
}
