package appmiddleware

import "github.com/gin-gonic/gin"

func ValidateAdmin(next func(ctx *gin.Context)) func(ctx *gin.Context) {
	return func(ctx *gin.Context) {
		//TODO Implement
		next(ctx)
	}
}

func ValidateAuth(next func(ctx *gin.Context)) func(ctx *gin.Context) {
	return func(ctx *gin.Context) {
		//TODO Implement
		next(ctx)
	}
}

func ValidateRecaptcha(next func(ctx *gin.Context)) func(ctx *gin.Context) {
	return func(ctx *gin.Context) {
		//TODO Implement
		next(ctx)
	}
}
