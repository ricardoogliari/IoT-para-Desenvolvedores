var five = require("johnny-five");
var board = new five.Board();

board.on("ready", function () {
    var proximity = new five.Proximity({
        controller: "HCSR04",
        pin: "A0"
    });
    
    proximity.on("data", function () {
        console.log("cm: ", this.cm);
    });
});