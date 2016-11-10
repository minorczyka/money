var crypto = require('crypto');
var secret = require('./consts').secret;

var generateSalt = function(length) {
  return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex')
    .slice(0,length);
};

var sha256 = function(password, salt) {
  var hash = crypto.createHmac('sha256', secret)
    .update(password + salt)
    .digest('hex');
  return hash;
};

module.exports = {
  generateSalt,
  sha256
}
