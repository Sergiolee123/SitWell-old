const mongoose = require('mongoose');

// models
require('./capture');

exports.Capture = mongoose.model('Capture');