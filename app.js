var express = require('express')
var http = require('http')
var app = express()
var mongoose = require('mongoose')
var bodyParser  = require('body-parser')
var server = http.createServer(app)

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

var io = require('socket.io')(server)

var waiters_name = new Array()
var waiters_id = new Array()
var rooms = []
var cards_order = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
var nums_order = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15]
var cards = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P']
var nums = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '+', '-', '*', '/']
var nums_num = nums.slice(0, 12)
var nums_op = nums.slice(-4)

Array.prototype.shuffle = function () {
    var length = this.length;

    while (length) {
        var index = Math.floor((length--) * Math.random());
        var temp = this[length];
        this[length] = this[index];
        this[index] = temp;
    }

    return this;
};

function makeTarget() {
  nums_num.shuffle()
  nums_op.shuffle()
  return nums_num[0] + nums_op[0] + nums_num[1]
}

io.sockets.on('connection', (socket) => {
  console.log('Socket connected : ' + socket.id)

  socket.on('waitBattle', (data) => {
    var waiter = data
    console.log('Im ' + waiter + 'waiting for battle')

    waiters_name.push(waiter)
    waiters_id.push(socket.id)
    console.log(waiters_name, waiters_id)
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

    socket.join(ask + "&" + accept)
  })

  socket.on('acceptGame', (ask, accept) => {
    var ask = ask
    var accept = accept
    var pos_ask = waiters_name.indexOf(ask)
    var pos_accept = waiters_name.indexOf(accept)

    // socket.to(waiters_id[pos_ask]).emit('startGame', ask, accept)

    socket.join(ask + "&" + accept)
    rooms.push(ask + "&" + accept)
    console.log(rooms[0] + "=", socket.rooms)
    io.to(ask + "&" + accept).emit('startGame', ask, accept, cards_order.shuffle(), nums_order.shuffle())

    setTimeout(() => {
      io.to(ask + "&" + accept).emit('startShow')
      setTimeout(() => {
        io.to(ask + "&" + accept).emit('stopShow')
        setTimeout(() => {
          var target = makeTarget()
          // while (target != parseInt(target)) {
          //   target = makeTarget()
          // }
          console.log("target: " + target)
          console.log("target eval: " + eval(target))
          io.to(ask + "&" + accept).emit('startRound', target, eval(target))
        }, 1000)
      }, 10000)
    }, 3000)

    // socket.to(waiters_id[pos_accept]).emit('startGame', ask, accept)

    console.log('Start game !' + ask + " and " + accept)

    if(pos_ask<pos_accept){
      waiters_id.splice(pos_accept, 1)
      waiters_name.splice(pos_accept, 1)
      waiters_id.splice(pos_ask, 1)
      waiters_name.splice(pos_ask, 1)
    }
    else{
      waiters_id.splice(pos_ask, 1)
      waiters_name.splice(pos_ask, 1)
      waiters_id.splice(pos_accept, 1)
      waiters_name.splice(pos_accept, 1)
    }

  })

  socket.on('click', (room, position) => {
    var room = room
    var position = position
    console.log(room + "   " + position)

    io.to(room).emit('opponentClick', position)
  })

  socket.on('turnOver', (one, two, three, targetString, targetNum, id, ask, accept, ask_scr, accept_scr) => {
    var room = ask + "&" + accept
    var one = one
    var two = two
    var three = three
    var targetNum = targetNum

    var exp = nums[one] + nums[two] + nums[three]
    console.log(exp)
    if (nums_op.includes(nums[one]) || nums_op.includes(nums[three])) {
      console.log("NOT VALID!")
      io.to(room).emit('wrong')
    } else {
      var ans = eval(exp)
      console.log(targetString)
      console.log(targetNum)

      if (ans == targetNum) {
        console.log("CORRECT!")
        io.to(room).emit('correct')
      } else {
        console.log("WRONG!")
        io.to(room).emit('wrong')
      }
    }
  })

  socket.on('endRound', () => {

  })

  socket.on('leave', (ask, accept, data)=>{
    var ask = ask
    var accept = accept
    var waiter = data
    socket.leave(ask + "&" + accept)
    console.log(socket.rooms)
    waiters_name.push(waiter)
    waiters_id.push(socket.id)
    console.log(waiters_name, waiters_id)
  })

  socket.on('disconnect', (data) => {
    console.log('Socket disconnect : ' + socket.id)

    if (waiters_id.includes(socket.id)) {
      var pos = waiters_id.indexOf(socket.id)

      var id = waiters_name[pos]
      UserAccount.findOne({id: id}, (err, result) =>{
        if(result){
          result.online = "false"
          result.save((error)=>{
            if (error) console.log("failed logout")
            else console.log("logout done")
          })
        }
        else if(err) console.log("can't find data")
      })


      waiters_name.splice(pos, 1)
      waiters_id.splice(pos, 1)
    }

  })

  socket.on('leave', (ask, accept, data)=>{
    var ask = ask
    var accept = accept
    var waiter = data
    socket.leave(ask + "&" + accept)
    console.log(socket.rooms)
    waiters_name.push(waiter)
    waiters_id.push(socket.id)
    console.log(waiters_name, waiters_id)


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
var Battle = require('./models/battle');


var router = require('./routes')(app, UserAccount, Battle);
