var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
var bodyParser = require('body-parser');

app.use(bodyParser.json()); // for parsing application/json
app.use(bodyParser.urlencoded({ extended: true }));

var users = {};

app.get('/', function (req, res) {
    res.send('Hello world!!');
});

app.post('/login', function (req, res) {
    var username = req.body.username;

    if(username != null){
        if(!(username in users)){
            users[username] = username;
            res.send({status: 'ok'});
        }else{
            res.send({error: 'El usuario ya existe'});
        }
    }
    console.log(users);
});

io.on('connection', function (socket) {
    socket.emit('news', { hello: 'world' });
    console.log("New user connected");
    socket.on('message', function (data) {
        socket.broadcast.emit("message", data);
        console.log(data);
    });
    socket.on('disconnect', function(data) {
        //console.log('User disconnect');
    })
});

server.listen(80);
