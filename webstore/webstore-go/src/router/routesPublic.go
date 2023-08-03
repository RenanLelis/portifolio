package router

import "github.com/gin-gonic/gin"

func ConfigPublicRoutes(r *gin.Engine) {
	r.POST("/api/public/test", func(ctx *gin.Context) {})
}
