var five = require("johnny-five");
var board = new five.Board();

board.on("ready", function () {
    // Plugue o modulo do sensor de temperature
    // na porta analógica 1 (A1) do Grove Shield
    var thermometer = new five.Thermometer({
        controller: "GROVE",
        pin: "A1"
    });

    // Plugue o modulo LCD em qualquer I2C no Grove Shield.
    var lcd = new five.LCD({
        controller: "JHD1313M1"
    });

    var f = 0;
    thermometer.on("data", function () {
        // A tela do LCD irá atualizar a cor de acordo com a temperatura.
        //
        // Quente -> Morno: Vermelho -> Amarelo
        // Moderado: Verde
        // Fresco -> Frio: Azul -> Violeta
        //
        // Experimente com fontes de temperatura quentes e frias!
        if (f === Math.round(this.celsius)) {
            return;
        }
        f = Math.round(this.celsius);
        var r = linear(0x00, 0xFF, f, 100);
        var g = linear(0x00, 0x00, f, 100);
        var b = linear(0xFF, 0x00, f, 100);
        lcd.bgColor(r, g, b).cursor(0, 0).print(f);
    });
});

// [Linear Interpolation](https://en.wikipedia.org/wiki/Linear_interpolation)
function linear(start, end, step, steps) {
    return (end - start) * step / steps + start;
}