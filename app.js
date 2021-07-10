const express = require('express')
const app = express()
var bodyParser  = require('body-parser');
//const mongoClient = require('mongodb').MongoClient
const mongoose = require('mongoose');



// [CONFIGURE APP TO USE bodyParser]
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

const url = "mongodb://localhost:27017"//몽고디비의

//app.use(express.json())//enabling json parsing in express

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

// app.listen(443, () => {
//     console.log("Listening...")
//
// })
var router = require('./routes')(app, UserAccount);

// [RUN SERVER]
var server = app.listen(443, function(){
 console.log("Express server has started on port " + 443)
});


app.get('/', (req, res) =>{
  res.send('히')
})
