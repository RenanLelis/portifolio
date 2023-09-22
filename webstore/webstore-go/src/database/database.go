package database

import (
	"context"
	"fmt"
	"log"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"renanlelis.github.io/portfolio/webstore-go/src/config"
)

func ConnectToDataBase() (*mongo.Client, error) {
	clientOptions := options.Client().ApplyURI(config.StringDataBaseConnection)
	client, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Fatal(err)
	}
	
	err = client.Ping(context.TODO(), nil)
	if err != nil {
		log.Fatal(err)
	}

	fmt.Println("Connected to MongoDB!")

	return client, err
}

// func GetDatabase(client *mongo.Client) *mongo.Database {
// 	return client.Database(config.DatabaseName)
// }

// func GetCollection(dataBase *mongo.Database, collectionName string) *mongo.Collection {
// 	return dataBase.Collection(collectionName)
// }

func GetCollection(client *mongo.Client, collectionName string) *mongo.Collection {
	return client.Database(config.DatabaseName).Collection(collectionName)
}