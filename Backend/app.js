// Carlos Linares
// App.js
// Conexion con la base de datos de mongo

// 8 jitomates
// brocoli
// 

var ws = require("ws").Server,
    wss = new ws({port:3000});
    
var bodyParser = require("body-parser");
   
var express = require("express"),
    app = express();
    
// CONFIG
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.listen(8000);

app.get("/", function(req, res){
   res.send("Hola Lakesh!!"); 
});
    
var MongoClient = require("mongodb").MongoClient;

    
MongoClient.connect('mongodb://localhost:27017/lakesh', function(err, db) { 
    if (err) { console.log("ERROR: " + err); }
    else {
        console.log("DONE: " + "conectado a Lakesh");
    }
});