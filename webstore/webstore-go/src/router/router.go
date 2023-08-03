package router

import (
	"github.com/gin-gonic/gin"
)

// ConfigRoutes will generate the routes and the controller functions
func ConfigRoutes(server *gin.Engine) {

	ConfigAuthRoutes(server)
	ConfigPublicRoutes(server)
	ConfigAdminRoutes(server)
	ConfigUserStoreRoutes(server)

}
