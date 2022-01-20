const mongoose = require("mongoose");

const dbURL = "mongodb+srv://oliveoil:oilolive@cluster0.iirvc.mongodb.net/SitWell?retryWrites=true&w=majority";

// Two Warning
mongoose.connect(dbURL, { useNewUrlParser: true,useUnifiedTopology: true });

const db = mongoose.connection;

db.once('open' ,() => {
	console.log('MongoDB Connection successful');
})

db.on('error', (error) => {
    console.error('Error in MongoDB connection: '+error);
    mongoose.disconnect();
});

db.on('close', function() {
    console.log('Database disconnection, reconnecting');
    mongoose.connect(dbURL);
});

module.exports = db;