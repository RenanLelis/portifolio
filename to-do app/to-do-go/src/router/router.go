package router

import (
	"github.com/gorilla/mux"
	"renan.com/todo/src/router/routes"
)

// Gerar vai retornar um router com as rotas configuradas
func GenerateRoutes() *mux.Router {
	r := mux.NewRouter()
	return routes.Config(r)
}
