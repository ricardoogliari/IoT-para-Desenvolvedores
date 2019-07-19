var express = require('express');
var app = express();
var server = app.listen(8085);
var five = require("johnny-five");
var board = new five.Board();

var led;
var valueTemp;
board.on("ready", function () {
    led = new five.Led(13);
    var temp = new five.Thermometer({
        controller: "GROVE",
        pin: "A0"
    });
    temp.on("data", function () {
        valueTemp = Math.round(this.celsius);
    });
});
app.get('/getTemp',
    function (requisicao, resposta) {
        resposta.send("A temperatura atual: " + valueTemp);
    }
);
app.get('/on',
    function (requisicao, resposta) {
        led.on();
        resposta.send('ligou o led');
    }
);
app.get('/off',
    function (requisicao, resposta) {
        led.off();
        resposta.send('desligou o led');
    }
);