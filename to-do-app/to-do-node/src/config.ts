import dotenv from 'dotenv';
dotenv.config();

export const HOST = (process.env.DB_HOST ? process.env.DB_HOST : "localhost");
export const USER = (process.env.DB_USER ? process.env.DB_USER : "todo");
export const PASSWORD = (process.env.DB_PASSWORD ? process.env.DB_PASSWORD : "Todo#123");
export const DB = process.env.DB_NAME ? process.env.DB_NAME : "todo";
