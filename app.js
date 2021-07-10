var express = require('express')
var http = require('http')
var app = express()
var mongoose = require('mongoose')
var bodyParser  = require('body-parser')
var server = http.createServer(app)

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var io = require('socket.io')(server)

io.sockets.on('connection', (socket) => {
  console.log('Socket connected : ${socket.id}')
  socket.on('clientMessage', (data) => {
    console.log('Client Message : ' + data)

    var message = {
      msg: 'server',
      data: 'data'
    }
    socket.emit('serverMessage', message)
  })
})

server.listen(443, () => {
  console.log('Server Listening...')
})


const url = "mongodb://localhost:27017"//몽고디비의

// [CONFIGURE ROUTER]
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function(){
    // CONNECTED TO MONGODB SERVER
    console.log("Connected to mongod server");
});


mongoose.connect(url+'/myDb', {
   useNewUrlParser: true
})

var UserAccount = require('./models/account');

var router = require('./routes')(app, UserAccount);
