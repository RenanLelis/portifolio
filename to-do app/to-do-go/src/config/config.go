package config

import (
	"fmt"
	"log"
	"os"
	"strconv"

	"github.com/joho/godotenv"
)

var (
	StringDataBaseConnection = ""

	Port = 0

	SecretKey []byte

	MailFrom     = ""
	SystemEmail  = ""
	MailPassword = ""
	SmtpPort     = ""
	SmtpHost     = ""
)

// Load will load the path variables
func Load() {
	var erro error

	if erro = godotenv.Load(); erro != nil {
		log.Fatal(erro)
	}

	Port, erro = strconv.Atoi(os.Getenv("API_PORT"))
	if erro != nil {
		Port = 8081
	}

	StringDataBaseConnection = fmt.Sprintf("%s:%s@/%s?charset=utf8&parseTime=True&loc=Local",
		os.Getenv("DB_USER"),
		os.Getenv("DB_PASSWORD"),
		os.Getenv("DB_NAME"),
	)

	SecretKey = []byte(os.Getenv("SECRET_KEY"))

	MailFrom = os.Getenv("MAIL_FROM")
	SystemEmail = os.Getenv("MAIL_FROM")
	MailPassword = os.Getenv("MAIL_PASSWORD")
	SmtpPort = os.Getenv("SMTP_PORT")
	SmtpHost = os.Getenv("SMTP_HOST")

}
