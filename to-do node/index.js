const express = require('express');
const cors = require("cors");
// const bodyParser = require('body-parser');
const routes = require('./controller/routes');
// const path = require('path');
require('dotenv').config()

const port = process.env.PORT || 5000;
const app = express();

//Allow this origins too make requests
var corsOptions = {
    origin: ["http://localhost:5000", "http://localhost:4200", "http://localhost:3000", "http://localhost:8080"]
};
app.use(cors(corsOptions));
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true }));
// app.use(express.static('public'));
app.use(routes.router);

app.listen(port, () => {
    console.log(`Executing on port ${port}`)
})