const express = require("express");
const http = require('http');
const app = express();
app.set('view engine', 'ejs');
app.use(express.json({limit: '5mb'}));

const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server, {
  cors: {
    origin: 'null',
    methods: ["GET", "POST"]
  }
});


app.get("/", (req, res) => {
    res.status(200).render("index");
  });

  io.on("connection", (socket) => {

    socket.on("join", (roomName) => {
      let room = io.sockets.adapter.rooms.get(roomName);
      //console.log(room)
      let userNum = room? room.size : 0;
      if(userNum < 2){
        socket.join(roomName)

        console.log('user ' + socket.id + ' join ' + roomName)
      }else{
        socket.emit('full', roomName);
      }

    })

    socket.on("send", (roomName, message) => {
      socket.to(roomName).emit('pm', message);
    })
  });

  server.listen(process.env.PORT || 8099);