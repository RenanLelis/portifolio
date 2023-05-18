import mysql from 'mysql';
import { DB, HOST, PASSWORD, USER } from '../config'

const connection = mysql.createPool({
    host: HOST,
    user: USER,
    password: PASSWORD,
    database: DB,
    waitForConnections: true,
    connectionLimit: 10,
});

export default connection;