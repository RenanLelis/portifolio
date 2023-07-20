package router

import (
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/controller/appmiddleware"
)

// AppRoute represents an struct for API routes
type AppRoute struct {
	URI            string
	Method         string
	Function       func(ctx *gin.Context)
	NeedsAdmin     bool
	NeedsAuth      bool
	NeedsRecaptcha bool
}

// ConfigRoutes will generate the routes and the controller functions
func ConfigRoutes(r *gin.Engine) {

	routes := authRoutes
	//TODO add other routes

	for _, route := range routes {
		if route.NeedsAdmin {
			r.Handle(route.Method, route.URI, appmiddleware.ValidateAdmin(route.Function))
		} else if route.NeedsAuth {
			r.Handle(route.Method, route.URI, appmiddleware.ValidateAuth(route.Function))
		} else if route.NeedsRecaptcha {
			r.Handle(route.Method, route.URI, appmiddleware.ValidateRecaptcha(route.Function))
		} else {
			r.Handle(route.Method, route.URI, route.Function)
		}
	}

}
