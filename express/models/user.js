var mongoose = require('mongoose');

var userSchema = mongoose.Schema({
  username: String,
  password: String,
  salt: String,
  name: String,
  email: String,
  lastLogin: { type: Date, default: Date.now }
});

var User = mongoose.model('User', userSchema);

module.exports = User;
