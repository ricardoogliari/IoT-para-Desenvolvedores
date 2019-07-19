var five = require("johnny-five"),
board = new five.Board();

var firebase = require('firebase');
var config = {
    apiKey: "AIzaSyDwgvMfbavcUDoWz_vCAPsvLWHr0NUiNhY",
    authDomain: "integracaofirebase-f9534.firebaseapp.com",
    databaseURL: "https://integracaofirebase-f9534.firebaseio.com",
    projectId: "integracaofirebase-f9534",
    storageBucket: "integracaofirebase-f9534.appspot.com",
messagingSenderId: "994372034290"
};
firebase.initializeApp(config);

var rootRef = firebase.database().ref("/lamp");

board.on("ready", function() {
    var rele = new five.Led(7);
    rootRef.on('value', function(snapshot) {
        if (snapshot.val()){
            rele.on();
        } else {
            rele.off();
        }
    });
});