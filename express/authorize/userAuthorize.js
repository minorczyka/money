var mongoose = require('mongoose');
var jwt = require('jsonwebtoken');

var secret = require('../authorize/consts').secret;

var authorize = function(req, res, next) {
  var token = req.get('Authorization');
  if (!token) {
    res.status(401).send('Missing token!');
    return;
  }
  jwt.verify(token, secret, function(err, decoded) {
    if (err) {
      res.status(401).send('Unaothorized!');
      return;
    }
    req.userId = decoded.userId;
    next();
  });
}

module.exports = authorize;
