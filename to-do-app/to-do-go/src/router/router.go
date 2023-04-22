package router

import (
	"github.com/gorilla/mux"
	"renanlelis.github.io/portfolio/to-do-go/src/router/routes"
)

// Gerar vai retornar um router com as rotas configuradas
func GenerateRoutes() *mux.Router {
	r := mux.NewRouter()
	return routes.Config(r)
}
