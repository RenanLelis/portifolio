package response

import (
	"encoding/json"
	"log"
	"net/http"
)

// JSON send a json formated response
func JSON(w http.ResponseWriter, statusCode int, data interface{}, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)
	if data != nil {
		if err := json.NewEncoder(w).Encode(data); err != nil {
			log.Fatal(err)
		}
	}
}

// Err send a formated error response
func Err(w http.ResponseWriter, statusCode int, err error, r *http.Request) {
	JSON(w, statusCode, errorMessage{
		ErrrorMessage: err.Error(),
	}, r)
}

type errorMessage struct {
	ErrrorMessage string `json:"errorMessage"`
}
