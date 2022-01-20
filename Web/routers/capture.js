const express = require('express');
const capture = require('../controllers/capture');
const router = express.Router()


router.get('/capture', capture.showCapturePage);
router.post('/captureSave', capture.newAndSave);
module.exports = router;