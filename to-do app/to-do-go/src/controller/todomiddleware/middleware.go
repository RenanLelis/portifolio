package todomiddleware

import (
	"encoding/json"
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
		newUserToken, err := security.RefreshTokenFromRequest(r)
		if err == nil {
			userDTO := response.CreateUserDTOFromJWT(newUserToken)
			if userDTO.ID > 0 {
				jsonUserDTO, err := json.Marshal(userDTO)
				if err == nil {
					w.Header().Set(security.AUTH, string(jsonUserDTO))
				}
			}
		}
		next(w, r)
	}
}
