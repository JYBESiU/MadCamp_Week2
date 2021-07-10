const mongoose = require('mongoose');

const UserAccountSchema = new mongoose.Schema({
  name: String,
  id: String,
  password: String,
  online: String
});

module.exports = mongoose.model("userAccount", UserAccountSchema);
