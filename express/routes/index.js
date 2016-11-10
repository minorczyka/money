var express = require('express');

var userAuthorize = require('../authorize/userAuthorize');

var router = express.Router();

router.use(userAuthorize);

/* GET home page. */
router.get('/', function(req, res, next) {
  res.send(req.userId);
});

module.exports = router;
