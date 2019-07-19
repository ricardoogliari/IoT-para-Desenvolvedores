const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

var payload = {
    data: {
        property: 'value'
    }
};

exports.stopAlarm = functions.https.onRequest((request, response) => {
    admin.database().ref('/token').once('value').then(
        function(snapshot) {
            var registrationToken = snapshot.val();

            admin.messaging().sendToDevice(registrationToken, payload)
                .then(success => {
                    response.send("{'success': true}");
                    return;
                })
                .catch(error => {
                    response.send("{'success': false}");
                    return;
                });

            return;
        }
    ).catch(error => {
        response.send("{'sucess': false}");
        return;
    });
 });
