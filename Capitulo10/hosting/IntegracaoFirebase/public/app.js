(function(){
  
    var config = {
        apiKey: "AIzaSyDvXTB-eFAcGEKTyozQ_W37vv3n56HoMKQ",
        authDomain: "integracaofirebase-1c3f0.firebaseapp.com",
        databaseURL: "https://integracaofirebase-1c3f0.firebaseio.com",
        projectId: "integracaofirebase-1c3f0",
        storageBucket: "integracaofirebase-1c3f0.appspot.com",
        messagingSenderId: "468356372458"
    };
    firebase.initializeApp(config);

    var db = firebase.database();

    var tempRef = db.ref('temperature');
    var humidRef = db.ref('humidity');
    var lampRef = db.ref('lamp');

    var currentLampValue = false;
    lampRef.on('value', function(snapshot){
        var value = snapshot.val();
        var el = document.getElementById('currentLamp')
        if(value){
            el.classList.add('amber-text');
        }else{
            el.classList.remove('amber-text');
        }
        currentLampValue = !!value;
    });

    var btnLamp = document.getElementById('btn-lamp');
    btnLamp.addEventListener('click', function(evt){
        lampRef.set(!currentLampValue);
    });

    tempRef.on('value', onNewData('currentTemp', 'tempLineChart', 'Temperatura', 'C°'));
    humidRef.on('value', onNewData('currentHumid', 'humidLineChart', 'Umidade', '%'));

})();


function onNewData(currentValueEl, chartEl, label, metric){
    return function(snapshot){
        var readings = snapshot.val();
        if(readings){
            var currentValue;
            var data = [];
            for(var key in readings){
                currentValue = readings[key]
                data.push(currentValue);
            }

            document.getElementById(currentValueEl).innerText = currentValue + ' ' + metric;
            buildLineChart(chartEl, label, data);
        }
    }
}

function buildLineChart(el, label, data){
    var elNode = document.getElementById(el);
    new Chart(elNode, {
        type: 'line',
        data: {
            labels: new Array(data.length).fill(""),
            datasets: [{
                label: label,
                data: data,
                borderWidth: 1,
                fill: false,
                spanGaps: false,
                lineTension: 0.1,
                backgroundColor: "#F9A825",
                borderColor: "#F9A825"
            }]
        }
    });
}
