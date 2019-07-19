var five = require("johnny-five");
var board = new five.Board();
var column = 1;
var row = 1;

var tsession = require("temboo/core/temboosession");
var session = new tsession.TembooSession("ricardoogliari", "myFirstApp", "sdssda4ZWGfjCOVlYQEMujrIDpHYBAq7");
var Google = require("temboo/Library/Google/Spreadsheets");
var updateCellsChoreo = new Google.UpdateCells(session);
var updateCellsInputs = updateCellsChoreo.newInputSet();

// Define entradas
updateCellsInputs.set_WorksheetId("od6");
updateCellsInputs.set_RefreshToken("ya29.Glv3BuuxYLBjwPTNV2MYMnK5lx2ssQv5wEkkGvOim6sENwFH8i7C9Jdir2LWMAU-NQe2gEnQdEoCEkanSk0PNA51Rtt2QbnoKfrrZTdaTOuHJp3CjvFnJpC-yObb");
updateCellsInputs.set_ClientSecret("Tmk4Jj6TIhiGPs8fbGGVc_PV");
updateCellsInputs.set_ClientID("311505055263-lhuv6q17hqqqp65g05ojm33j5eh24190.apps.googleusercontent.com");
updateCellsInputs.set_SpreadsheetKey("1MsThj9zEfc3Rc1euOCnfiroul9qJRnKb-b3kAEFaX6E");
updateCellsInputs.set_Column("1");

board.on("ready", function() {
    var button = new five.Button(2);

    button.on("press", function() {
        var now = today=new Date();
        updateCellsInputs.set_InputValue("Geladeira aberta " +
            now.getDate ()  + "/" + (now.getMonth() + 1) + "/" + now.getFullYear () +
            " - "  + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());
        updateCellsInputs.set_Row(row);

        row++;
        updateCellsChoreo.execute(
            updateCellsInputs,
            function(results){
                console.log(results.get_NewAccessToken());
            },
            function(error){console.log(error.type); console.log(error.message);}
        );
    });

    button.on("release", function() {
        var now = today=new Date();
        updateCellsInputs.set_InputValue("Geladeira fechada " +
            now.getDate ()  + "/" + (now.getMonth() + 1) + "/" + now.getFullYear () +
            " - "  + now.getHours() + ":" + now.getMinutes() + ":" + now.getSeconds());
        updateCellsInputs.set_Row(row);

        row++;
        updateCellsChoreo.execute(
            updateCellsInputs,
            function(results){
                console.log(results.get_NewAccessToken());
            },
            function(error){console.log(error.type); console.log(error.message);}
        );
    });
});
