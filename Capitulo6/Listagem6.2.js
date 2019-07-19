var five = require("johnny-five");
var board = new five.Board();
var send = true;

var tsession = require("temboo/core/temboosession");
var session = new tsession.TembooSession("ricardoogliari", "myFirstApp", "xAB12lxS09PGsoABdCymiegEnbxk9LRb");
var Google = require("temboo/Library/Google/Gmailv2/Messages");
var sendMessageChoreo = new Google.SendMessage(session);
var sendMessageInputs = sendMessageChoreo.newInputSet();

sendMessageInputs.set_RefreshToken("ya29.GltDB_oJsjp-zRBb2eY1etpelVDQ3EugYE2NsTdeIDNsyoPdsQz80tlwtnPWQmjRU8vUX3Rg3wNU9mEHUzkK67zoLem03-UA6Fyv1g8hyW4DhkRpZvfg-qAbCSTA");
sendMessageInputs.set_ClientSecret("u_bvresnFgQL8zJuTzHKdEG7");
sendMessageInputs.set_ClientID("311505055263-g071ae9a3fn612jghcdmeh69cdjt0f7l.apps.googleusercontent.com");
sendMessageInputs.set_From("rogliariping@gmail.com ");
sendMessageInputs.set_To("rogliariping@gmail.com");
sendMessageInputs.set_Subject("Cuidado");
sendMessageInputs.set_MessageBody("Alguém está se aproximando de você, muito cuidado!!!!");

board.on("ready", function () {
    var proximity = new five.Proximity({
        controller: "HCSR04",
        pin: "A0"
    });

    proximity.on("data", function () {
        if (this.cm < 2 && this.cm > 0) {
            if (send) {
                send = false;
                sendMessageChoreo.execute(
                    sendMessageInputs,
                    function (results) {
                        console.log(results.get_NewAccessToken());
                        setTimeout(function () {
                            send = true;
                        }, 15000);
                    },
                    function (error) {
                        console.log(error.type);
                        console.log(error.message);
                        send = true;
                    }
                );
            }
        }
    });
});