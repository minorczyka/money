var express = require('express');
var jwt = require('jsonwebtoken');

var User = require('../models/user');
var hash = require('../authorize/hash');
var secret = require('../authorize/consts').secret;

var router = express.Router();

router.post('/signUp', function(req, res, next) {
  if (!req.body.username || !req.body.password || !req.body.name || !req.body.email) {
    res.status(404).send();
    return;
  }
  User.findOne({ username: req.body.username }, function(err, user) {
    if (err) {
      res.status(404).send(err)
      return;
    }
    if (user) {
      res.status(404).send('User ' + req.body.username + ' already exists!');
      return;
    }
    var salt = hash.generateSalt(16);
    var passwordHash = hash.sha256(req.body.password, salt);
    var newUser = new User({
      username: req.body.username,
      password: passwordHash,
      salt: salt,
      name: req.body.name,
      email: req.body.email
    });
    newUser.save(function(err) {
      if (err) {
        res.status(404).send(err);
      } else {
        res.send('ok');
      }
    });
  });
});

router.post('/signIn', function(req, res, next) {
  if (!req.body.username || !req.body.password) {
    res.status(404).send();
    return;
  }
  User.findOne({ username: req.body.username }, function(err, user) {
    if (err) {
      res.status(404).send(err);
      return;
    }
    if (!user) {
      res.status(401).send('User ' + req.body.username + ' not found!');
      return;
    }
    var passwordHash = hash.sha256(req.body.password, user.salt);
    if (passwordHash === user.password) {
      var token = jwt.sign({ userId: user._id }, secret, { expiresIn: '1h' });
      res.send({ token: token });
    } else {
      res.status(401).send('Wrong password!');
      return;
    }
  });
});

module.exports = router;
