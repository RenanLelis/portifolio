package main

import (
	"fmt"
	"log"
	"net/http"

	"github.com/rs/cors"
	"renanlelis.github.io/portfolio/to-do-go/src/config"
	"renanlelis.github.io/portfolio/to-do-go/src/router"
)

// Just generate a random encoding key
// func init() {
// 	chave := make([]byte, 64)
// 	if _, erro := rand.Read(chave); erro != nil {
// 		log.Fatal(erro)
// 	}
// 	stringBase64 := base64.StdEncoding.EncodeToString(chave)
// 	fmt.Println(stringBase64)
// }

func main() {
	fmt.Println("Starting application")
	config.Load()
	r := router.GenerateRoutes()
	handler := cors.AllowAll().Handler(r)
	// handler := cors.Default().Handler(r)
	fmt.Printf("Listenning on port %d\n", config.Port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", config.Port), handler))
}
