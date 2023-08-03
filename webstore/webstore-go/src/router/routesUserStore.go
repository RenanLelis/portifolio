package router

import (
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/appmiddleware"
)

func ConfigUserStoreRoutes(r *gin.Engine) {
	r.POST("/api/store/test", appmiddleware.ValidateAuth(), func(ctx *gin.Context) {})
}
