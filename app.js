var express = require('express')
var http = require('http')
var app = express()
// var mongoClient = require('mongodb').MongoClient
var mongoose = require('mongoose')
var bodyParser  = require('body-parser')
var server = http.createServer(app)

var io = require('socket.io')(server)

var waiters_name = new Array()
var waiters_id = new Array()
var rooms = ["1", "2", "3", "4", "5"]

io.sockets.on('connection', (socket) => {
  console.log('Socket connected : ' + socket.id)

  socket.on('waitBattle', (data) => {
    var waiter = data
    console.log('Im ' + waiter + 'waiting for battle')

    waiters_name.push(waiter)
    waiters_id.push(socket.id)
  })

  socket.on('battle', (data) => {
    var battleData = JSON.parse(data)
    var ask = battleData.ask
    var accept = battleData.accept

    console.log(ask + 'make battle to ' + accept)

    if (waiters_name.includes(accept)) {
      var pos = waiters_name.indexOf(accept)
      socket.to(waiters_id[pos]).emit("challengeCome", ask, accept)
    }
  })

  socket.on('acceptGame', (ask, accept) => {
    var ask = ask
    var accept = accept
    var pos_ask = waiters_name.indexOf(ask)
    var pos_accept = waiters_name.indexOf(accept)

    socket.join(ask + "&" + accept)

    socket.to(waiters_id[pos_ask]).emit('startGame', ask, accept)
    // socket.to(waiters_id[pos_accept]).emit('startGame', ask, accept)

    console.log('Start game !' + ask + " and " + accept)

    waiters_id = waiters_id.splice(pos_ask, 1)
    waiters_id = waiters_id.splice(pos_accept, 1)
    waiters_name = waiters_name.splice(pos_ask, 1)
    waiters_name = waiters_name.splice(pos_accept, 1)
  })

  socket.on('disconnect', (data) => {
    console.log('Socket disconnect : ' + socket.id)

    if (waiters_id.includes(socket.id)) {
      var pos = waiters_id.indexOf(socket.id)
      waiters_name = waiters_name.splice(pos, 1)
      waiters_id = waiters_id.splice(pos, 1)
    }
  })

})

server.listen(443, () => {
  console.log('Server Listening...')
})


//
// const url = "mongodb://localhost:27017/"//몽고디비의
//
// // var server = app.listen(443, () => {
// //     console.log("Listening...")
// // })
//
// app.use(bodyParser.urlencoded({ extended: true }));
// app.use(bodyParser.json());
//
// var db = mongoose.connection
// db.on('error', console.error)
// db.once('open', () => {
//   console.log('connecting mongoDB server')
// })
//
// mongoose.connect(url+'myDb')
//
// var Battle = require('./models/battle')
//
// var router = require('./routes')(app, Battle)





 /* mongoClient.connect(url, (err, db) => {//url to server,two parameter of callback function

    if (err) {
        console.log("Error while connecting mongo client")
    } else {

        const myDb = db.db('myDb')//myDb라는 디비
        const collection = myDb.collection('UserAccount')

        app.post('/signup', (req, res) => {

            const newUser = {
                name: req.body.name,
                id: req.body.id,
                password: req.body.password
            }

            const query = { id: newUser.id }//뉴 유저의

            collection.findOne(query, (err, result) => {//이메일 있는지

                if (result == null) {//디비에
                    collection.insertOne(newUser, (err, result) => {//디비에
                        res.status(200).send()
                    })
                } else {
                    res.status(400).send()
                }

            })

        })

        app.post('/login', (req, res) => {

            const query = {
                id: req.body.id,
                password: req.body.password
            }

            collection.findOne(query, (err, result) => {

                if (result != null) {

                    const objToSend = {//유저 정보 찾아진거니까 거기의 이름과 이메일 리턴
                        name: result.name,
                        id: result.id
                    }

                    res.status(200).send(JSON.stringify(objToSend))//스트링으로 바꿔 보냄.

                } else {
                    res.status(404).send()
                }

            })

        })

    }

}) */
