import express, { Express } from 'express';
import dotenv from 'dotenv';
import cors from 'cors';
import routesAuth from './controller/routes/routesAuth'
import routesUser from './controller/routes/routesUser'
import routesTasks from './controller/routes/routesTasks'

dotenv.config();

const app: Express = express();
const port = process.env.API_PORT || 8080;

var corsOptions = {
    origin: ["http://localhost:5000", "http://localhost:5173", "http://localhost:4200", "http://localhost:8080"]
};
app.use(cors(corsOptions));
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ extended: true }));

app.use(routesAuth.router);
app.use(routesUser.router);
app.use(routesTasks.router);

app.listen(port, () => {
    console.log(`Server running at port ${port}`);
});