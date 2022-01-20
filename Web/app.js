//const cors = require('cors');
const express = require('express');
const router = require('./routers/index');
const db = require('./mongodb/db');
const bodyParser = require('body-parser');
// enable CORS - Cross Origin Resource Sharing

const app = express();
const port = process.env.PORT || 8099;

//app.use(cors());
app.set('view engine', 'ejs');
app.use(bodyParser.json({limit: '5mb'}));
app.use(bodyParser.urlencoded({limit: '5mb', extended: true}));
app.use('/',router);
app.use(express.static(__dirname + '/public'));
app.use(express.json());
app.listen(port);