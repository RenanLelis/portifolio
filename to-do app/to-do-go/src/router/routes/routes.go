package routes

import (
	"net/http"

	"github.com/gorilla/mux"
	"renan.com/todo/src/controller/todomiddleware"
)

// Route represents an struct for API routes
type Route struct {
	URI       string
	Method    string
	Function  func(http.ResponseWriter, *http.Request)
	NeedsAuth bool
}

// Config gets the api routes and the functions that will handle the calls to those routes
func Config(r *mux.Router) *mux.Router {
	routes := authRoutes
	routes = append(routes, userRoutes...)
	routes = append(routes, tasksRoutes...)

	for _, route := range routes {
		if route.NeedsAuth {
			r.HandleFunc(route.URI, todomiddleware.ValidateAuth(route.Function)).Methods(route.Method)
		} else {
			r.HandleFunc(route.URI, route.Function).Methods(route.Method)
		}
	}

	return r

}
