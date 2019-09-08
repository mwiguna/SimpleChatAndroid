
const express = require('express'),
http = require('http'),
app = express(),
server = http.createServer(app),
io = require('socket.io').listen(server);

app.get('/', (req, res) => {
  res.send("Server running");
});

// --------- Socket

io.on('connection', (socket) => {
  console.log('user connected')

  // --- This one would used

  socket.on('messagedetection', (sendername, message, imgUrl) => {
        console.log(sendername+" : " + message+" : " + imgUrl)
        let msg = {"message": message, "sendername": sendername, "imgUrl": imgUrl}

         // send the message to all users including the sender using io.emit()
        io.emit('newmessage', msg);
        //io.broadcast.emit(eventName, args)
  });

  socket.on('imagedetection', (img) => {
        console.log(img)
        io.emit('newimg', img);
  });

});

// --------- Listen

server.listen(3000, () => {
  console.log('Node app is running on port 3000')
});

// npm install --save express socket.io
// Run this file : node index.js
// Change URLServer on MainActivity
