const express = require("express")
const Websocket = require('ws')
const http = require('http');

const app = express()
app.set('view engine', 'ejs')

app.use(express.json({limit: '5mb'}))

const server = http.createServer(app);
const wss = new Websocket.Server({ server })

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

wss.on('connection', (ws) => {
    console.log('One client connected')
    ws.on("message", msg => {
      if(msg+"" === 'ping'){
        ws.send('ping')
      }
    })
  })

  server.listen(process.env.PORT || 25565, () => {
    console.log(`Server started on port ${server.address().port}`);
});