var eventBus = function(_eventBusURL){ 
    var eventBus = new EventBus(_eventBusURL);
    return eventBus;
}

var eventBusOpen = function(_eventBusURL, _address){
    var eventBus = new EventBus(_eventBusURL);
    eventBus.onopen = function () {        
        eventBus.registerHandler(_address, function (error, message) {            
            var jsonMessage = JSON.parse(JSON.stringify(message.body));
            var inputMessage = JSON.parse(JSON.stringify(jsonMessage.inputMessage));                        
            $('#input').append("<div class='speech-bubble'><p class='speech-bubble-text'>" + inputMessage).append('</p></div>');
        });
    }
    eventBusClose(eventBus);
}

var eventBusSend = function(_eventBusURL, _address, $message){
    var eventBus = new EventBus(_eventBusURL);
    eventBus.onopen = function () {        
        eventBus.send(_address, $message);
    }
    eventBusClose(eventBus);
}

var eventBusClose = function($eventBus){    
    $eventBus.onclose = function(err) {
        alert('Error while trying to connect to the server: ' + err.reason + " with state: " + eventBus.state); // this does happen 
    }
}