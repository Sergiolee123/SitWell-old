const express = require('express');
const router = express.Router();
const captureRoute = require('./capture')

//router.use('/capture',capture);
router.use('/', captureRoute);
module.exports = router;