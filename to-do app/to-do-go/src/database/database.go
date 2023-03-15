package database

import (
	"database/sql"

	_ "github.com/go-sql-driver/mysql"
	"renan.com/todo/src/config"
)

// ConnectToDataBase opens a connection to the MySql Database
func ConnectToDataBase() (*sql.DB, error) {
	db, erro := sql.Open("mysql", config.StringDataBaseConnection)
	if erro != nil {
		return nil, erro
	}
	if erro = db.Ping(); erro != nil {
		db.Close()
		return nil, erro
	}
	return db, nil
}
