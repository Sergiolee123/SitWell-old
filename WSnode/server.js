const express = require("express")
const Websocket = require('ws')
const app = express()

app.set('view engine', 'ejs')
const wss = new Websocket.Server({ port: 25565 })
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


app.listen(8099)