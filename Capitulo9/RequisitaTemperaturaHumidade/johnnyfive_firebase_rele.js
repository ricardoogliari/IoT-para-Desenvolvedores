var five = require("johnny-five"),
board = new five.Board();

var firebase = require('firebase');
var config = {
    apiKey: "AIzaSyDvXTB-eFAcGEKTyozQ_W37vv3n56HoMKQ",
    authDomain: "integracaofirebase-1c3f0.firebaseapp.com",
    databaseURL: "https://integracaofirebase-1c3f0.firebaseio.com",
    projectId: "integracaofirebase-1c3f0",
    storageBucket: "integracaofirebase-1c3f0.appspot.com",
    messagingSenderId: "468356372458"
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
