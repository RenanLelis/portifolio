package appmiddleware

import "github.com/gin-gonic/gin"

func ValidateAuth() gin.HandlerFunc {
	return func(ctx *gin.Context) {
		//TODO Implement
		// before request
		ctx.Next()
		// after request
	}
}

func ValidateAdmin() gin.HandlerFunc {
	return func(ctx *gin.Context) {
		//TODO Implement
		// before request
		ctx.Next()
		// after request
	}
}
