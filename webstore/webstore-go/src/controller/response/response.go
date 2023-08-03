package response

import (
	"github.com/gin-gonic/gin"
)

type errorMessage struct {
	ErrrorMessage string `json:"errorMessage"`
}

// JSON send a json formated response
func JSON(ctx *gin.Context, statusCode int, data interface{}) {
	ctx.Header("Content-Type", "application/json")
	ctx.IndentedJSON(statusCode, data)
}

// Err send a formated error response
func Err(ctx *gin.Context, statusCode int, err error) {
	JSON(ctx, statusCode, errorMessage{ ErrrorMessage: err.Error() })
}