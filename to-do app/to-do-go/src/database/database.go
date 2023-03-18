package database

import (
	"database/sql"

	_ "github.com/go-sql-driver/mysql"
	"renan.com/todo/src/config"
)

// ConnectToDataBase opens a connection to the MySql Database
func ConnectToDataBase() (*sql.DB, error) {
	db, err := sql.Open("mysql", config.StringDataBaseConnection)
	if err != nil {
		return nil, err
	}
	if err = db.Ping(); err != nil {
		db.Close()
		return nil, err
	}
	return db, nil
}
