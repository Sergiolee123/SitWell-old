const express = require("express")
const Websocket = require('ws')
const https = require('https');
const fs = require('fs');

const app = express()

app.set('view engine', 'ejs')

const server = https
.createServer(
  {
    // ...
    cert: fs.readFileSync('public-cert.pem'),
    key: fs.readFileSync('private-key.pem'),
    // ...
  },
  app
)
.listen(25565);

const wss = new Websocket.Server({ server })
app.use(express.json({limit: '5mb'}))


wss.on('connection', (ws) => {
    console.log('One client connected')
    ws.on("message", msg => {
      ws.send(`msg is = ${msg}`)
    })
  })


  app.get("/", (req, res) => {
    res.status(200).render("index");
  })
  
  // handles bot request
  app.post("/captureSave", (req, res) => {
    // Broadcast URL to connected ws clients
    console.log(req.body)
    //console.log(req.body.image)
    wss.clients.forEach((client) => {
      // Check that connect are open and still alive to avoid socket error
      if (client.readyState === Websocket.OPEN) {
        client.send(req.body.image)
      }
    });
  
  });


 https
  .createServer(
    {
      // ...
      cert: fs.readFileSync('public-cert.pem'),
      key: fs.readFileSync('private-key.pem'),
      // ...
    },
    app
  )
  .listen(8099);