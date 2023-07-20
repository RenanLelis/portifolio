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
	Port                     = 0
	SecretKey                []byte
	MailFrom                 = ""
	SystemEmail              = ""
	MailPassword             = ""
	SmtpPort                 = 0
	SmtpHost                 = ""
)

// Load will load the path variables
func Load() {
	var err error

	if err = godotenv.Load(); err != nil {
		log.Fatal(err)
	}

	Port, err = strconv.Atoi(os.Getenv("API_PORT"))
	if err != nil {
		Port = 8080
	}

	StringDataBaseConnection = fmt.Sprintf("%s:%s@/%s?charset=utf8&parseTime=True&loc=Local",
		os.Getenv("DBgodotenv"),
		os.Getenv("DB_PASSWORD"),
		os.Getenv("DB_NAME"),
	)

	SecretKey = []byte(os.Getenv("SECRET_KEY"))

	MailFrom = os.Getenv("MAIL_FROM")
	SystemEmail = os.Getenv("MAIL_FROM")
	MailPassword = os.Getenv("MAIL_PASSWORD")
	SmtpPort, err = strconv.Atoi(os.Getenv("MAIL_SMTP_PORT"))
	if err != nil {
		SmtpPort = 465
	}
	SmtpHost = os.Getenv("MAIL_SMTP_HOST")

}
