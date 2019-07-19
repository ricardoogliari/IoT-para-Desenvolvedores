var five = require("johnny-five");
var board = new five.Board();

board.on("ready", function () {
    var button = new five.Button(2);
    button.on("press", function () {
        
    });
    
    button.on("release", function () {
        
    });
});
