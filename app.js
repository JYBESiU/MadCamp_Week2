var express = require('express')
var http = require('http')
var app = express()
// var mongoClient = require('mongodb').MongoClient
var mongoose = require('mongoose')
var bodyParser  = require('body-parser')
var server = http.createServer(app)

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
