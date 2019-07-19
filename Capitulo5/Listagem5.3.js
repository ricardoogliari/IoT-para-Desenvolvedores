var express = require('express');
var app = express();
var server = app.listen(8085);

var five = require("johnny-five");
var board = new five.Board();

var led;

board.on("ready", function () {
    led = new five.Led(7);
});

app.get('/on',
    function (requisicao, resposta) {
        led.on();
        resposta.send('lâmpada ligada');
    }
);

app.get('/off',
    function (requisicao, resposta) {
        led.off();
        resposta.send('lâmpada desligada');
    }
);