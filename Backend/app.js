// Carlos Linares
// App.js
// Conexion con la base de datos de mongo

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
    
    // TODO: Mongo - LISTO
    // Funcion para obtener todos los colchones
    // Funcion para obtener un colchon por id de beacon
    
    function selectColchones(callback){
        var cursor = db.collection('colchones').find();
        var retorno = [];
        
        cursor.each(function(err, item) {
            if (item == null) {
                callback(retorno);
            }
            else {
                retorno.push(item);
            }
        });
    }
    
    function selectColchonByBeaconId(id, callback){
        db.collection('colchones').findOne({'beacon':id},function(err, data){
            if (err){console.log(err);}
            if (data){
                callback(data);
            }
        });
    }
    
    // TODO: Express - LISTO
    // Web Service para retornar todos los colchones
    // Web Service para retornar un colchon por id de beacon
    
    app.get("/getColchones", function(req, res){
        selectColchones(function(ret){
            res.send(JSON.stringify(ret));
        });
    });
    
    app.get("/getColchonById", function(req, res){
        if (req.headers.beacon){
            selectColchonByBeaconId(req.headers.beacon, function(ret){
                res.send(JSON.stringify(ret));
            });
        }
        else{
            res.send(JSON.stringify({error:"Beacon Header no especificado"}));
        }
    });
});