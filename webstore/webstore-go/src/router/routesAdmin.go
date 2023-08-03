package router

import (
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/appmiddleware"
)

func ConfigAdminRoutes(r *gin.Engine) {
	r.POST("/api/admin/test", appmiddleware.ValidateAdmin(), func(ctx *gin.Context) {})
}
