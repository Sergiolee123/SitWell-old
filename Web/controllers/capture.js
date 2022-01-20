const models    = require('../models');
const Capture   = models.Capture;
exports.showCapturePage = (req, res) => {
    res.render('index');
};

//crud
//Creat a new document
exports.newAndSave = (req, res) => {
    var data = req.body;
    console.log('Incoming new capture');

    var capture  = new Capture();
    capture.base64 = data.image;
    capture.save( (err) => {
        console.log(err);
    });
    res.status(200).json({
       message : 'sent'
    });
}

// Read Capture
exports.readCapture = (req, res) => {

    var capture  = new Capture();
    capture.find({}).sort({_id:-1}.limit(1));
}

// Remove All Capture
exports.deleteAllcapture = (req, res) => {

}