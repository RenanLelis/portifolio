package main

import (
	"crypto/rand"
	"encoding/base64"
	"fmt"
	"log"
	"net/http"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"renanlelis.github.io/portfolio/webstore-go/src/config"
	"renanlelis.github.io/portfolio/webstore-go/src/router"
)

// Just generate a random encoding key
func init() {
	chave := make([]byte, 64)
	if _, erro := rand.Read(chave); erro != nil {
		log.Fatal(erro)
	}
	stringBase64 := base64.StdEncoding.EncodeToString(chave)
	fmt.Println(stringBase64)
}

func main() {
	fmt.Println("Starting application")
	config.Load()
	r := gin.Default()
	router.ConfigRoutes(r)
	r.Use(cors.New(cors.DefaultConfig()))
	fmt.Printf("Listenning on port %d\n", config.Port)
	// r.Run(fmt.Sprintf(":%d", config.Port))
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", config.Port), r))
}
