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
var pass = []
var push = []
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
    var ask_name = battleData.name
    var accept_name = battleData.accept_name

    console.log(ask + 'make battle to ' + accept)

    if (waiters_name.includes(accept)) {
      var pos = waiters_name.indexOf(accept)
      socket.to(waiters_id[pos]).emit("challengeCome", ask, accept, ask_name)
    }

    socket.join(ask + "&" + accept)
  })

  socket.on('acceptGame', (ask, accept, ask_name, accept_name) => {
    var ask = ask
    var accept = accept
    var pos_ask = waiters_name.indexOf(ask)
    var pos_accept = waiters_name.indexOf(accept)

    var ask_name = ask_name
    var accept_name = accept_name

    // socket.to(waiters_id[pos_ask]).emit('startGame', ask, accept)

    socket.join(ask + "&" + accept)
    rooms.push(ask + "&" + accept)
    pass.push(0)
    push.push(0)

    console.log(rooms[0] + "=", socket.rooms)
    console.log(Battle.count())

    var cnt =0;
    Battle.count({}, (err, count) =>{
      if(err) return console.log("error")
      cnt = count
    })



    io.to(ask + "&" + accept).emit('startGame', ask, accept, cards_order.shuffle(), nums_order.shuffle(), cnt, ask_name, accept_name)

    setTimeout(() => {
      io.to(ask + "&" + accept).emit('startShow')
      setTimeout(() => {
        io.to(ask + "&" + accept).emit('stopShow')
        setTimeout(() => {
          var target = makeTarget()
          while (eval(target) != parseInt(eval(target))) {
            target = makeTarget()
          }
          io.to(ask + "&" + accept).emit('startRound', target, eval(target), 0, 0)
        }, 1000)
      }, 10000) // 보여주는 시
    }, 3000)

    // socket.to(waiters_id[pos_accept]).emit('startGame', ask, accept)

    console.log('Start game !' + ask + " and " + accept)

  })

  socket.on('challengeReject', (id, ask, accept) => {
    if (id == ask) {
      var pos = waiters_name.indexOf(accept)
      socket.to(waiters_id[pos]).emit("yourRejected")

    } else if (id = accept) {
      var pos = waiters_name.indexOf(ask)
      socket.to(waiters_id[pos]).emit("yourRejected")
    }
  })

  socket.on('click', (room, position, size, clicker) => {
    var room = room
    var position = position
    console.log(room + "   " + position)

    io.to(room).emit('opponentClick', position, size, clicker)
  })


  socket.on('startTurn', (room, id) => {
    var pos = rooms.indexOf(room)
    if (push[pos] == 0) {
      push[pos] = 1
      socket.broadcast.to(room).emit('opponentTurn', id)
    }

  })

  socket.on('passTurn', (room, id, ask, accept, ask_scr, accept_scr) => {
    console.log("pass")
    var pos = rooms.indexOf(room)
    pass[pos] += 1

    if (pass[pos] == 1) {
      socket.broadcast.to(room).emit('opponentPass', id, ask, accept)
    }

    if (pass[pos] == 2) {
      pass[pos] = 0
      push[pos] = 0
      var ask_scr = ask_scr
      var accept_scr = accept_scr

      var target = makeTarget()
      while (eval(target) != parseInt(eval(target))) {
        target = makeTarget()
      }
      io.to(room).emit('startRound', target, eval(target), ask_scr, accept_scr)
    }
  })

  socket.on('endTurn', (one, two, three, targetString, targetNum, id, ask, accept, ask_scr, accept_scr) => {
    var room = ask + "&" + accept
    var one = one
    var two = two
    var three = three
    var targetNum = targetNum

    var exp = nums[one] + nums[two] + nums[three]
    console.log(exp)
    if (nums_op.includes(nums[one]) || nums_op.includes(nums[three])) {
      var pos = rooms.indexOf(room)
      push[pos] = 0
      console.log("NOT VALID!")
      io.to(room).emit('wrong')
    } else {
      var ans = eval(exp)
      if (ans == targetNum) {
        console.log("CORRECT!")
        if (id == ask) {
          io.to(room).emit('correct', ask_scr + 1, accept_scr)
        } else if (id == accept) {
          io.to(room).emit('correct', ask_scr, accept_scr + 1)
        }
      } else {
        var pos = rooms.indexOf(room)
        push[pos] = 0
        console.log("WRONG!")
        io.to(room).emit('wrong')
      }
    }
  })


  socket.on('endRound', (room, ask, accept, ask_scr, accept_scr, battleid, ask_name, accept_name, stop, passFlag) => {
    var ask_scr = ask_scr
    var accept_scr = accept_scr

    var pos_ask = waiters_name.indexOf(ask)
    var pos_accept = waiters_name.indexOf(accept)
    var id_ask = waiters_id[pos_ask]
    var id_accept = waiters_id[pos_accept]

    var ask_name = ask_name
    var accept_name = accept_name

    var stop = stop

    if(stop=="stop"){
      io.to(id_ask).emit('stop')
      io.to(id_accept).emit('stop')
      console.log("stop")
    }


    if (ask_scr == 1) {
      io.to(id_ask).emit('win')
      io.to(id_accept).emit('lose')
      console.log("askwin")
      // update winner,
      //battleid 필요

      var battle = new Battle()
      battle.battleid = battleid
      battle.ask = ask
      battle.accept = accept
      battle.winner=ask_name
      battle.loser=accept_name
      battle.ask_scr= ask_scr
      battle.accept_scr=accept_scr

      battle.save((err)=>{
        if(err) console.log("battleDB fail");
        console.log("made battle")
      })


    } else if (accept_scr == 1) {
      io.to(id_ask).emit('lose')
      io.to(id_accept).emit('win')
      console.log("losewin")

      var battle = new Battle()
      battle.battleid = battleid
      battle.ask = ask
      battle.accept = accept
      battle.winner=accept_name
      battle.loser=ask_name
      battle.ask_scr= ask_scr
      battle.accept_scr=accept_scr

      battle.save((err)=>{
        if(err) console.log("battleDB fail");
        console.log("made battle")
      })
      // update winner,


    } else {
      var target = makeTarget()
      while (eval(target) != parseInt(eval(target))) {
        target = makeTarget()
      }
      var pos = rooms.indexOf(room)
      pass[pos] = 0
      push[pos] = 0
      io.to(room).emit('startRound', target, eval(target), ask_scr, accept_scr)
    }
  })


  socket.on('leave', (ask, accept, data)=>{
    var ask = ask
    var accept = accept
    var waiter = data
    socket.leave(ask + "&" + accept)
    console.log(socket.rooms)

    console.log(waiters_name, waiters_id)
  })

  socket.on('sendEmoji', (room, data)=>{
    console.log('got Emoji')

    socket.broadcast.to(room).emit('emoji', data)
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
