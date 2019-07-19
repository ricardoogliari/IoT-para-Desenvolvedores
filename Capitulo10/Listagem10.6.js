var five = require("johnny-five"),
board = new five.Board();

board.on("ready", function() {
    var rele = new five.Led(7);
    rele.on();
    rele.off();
});