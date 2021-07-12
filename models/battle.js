var mongoose = require('mongoose')
var Schema = mongoose.Schema

var battleSchema = new Schema({
  ask: String,
  accept: String,
  winner: String,
  loser: String
})

module.exports = mongoose.model('battle', battleSchema)
