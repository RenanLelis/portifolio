require('dotenv').config();

module.exports = {
    HOST: process.env.HOST ? process.env.HOST : "localhost",
    USER: process.env.USER ? process.env.USER : "todo",
    PASSWORD: process.env.PASSWORD ? process.env.PASSWORD : "todo#123",
    DB: process.env.DB ? process.env.DB : "todo"
};