$(function() {
    eventBusOpen(_eventBusURL, 'chat-output-posted');
});

var _eventBusURL = 'http://localhost:7770/eventbus';
var _address = 'chat-output';

    $('#sendMessage').click(function(){
        if(NotEmpty(message()) == true){          
            var json = {};
            json.inputMessage = message();
            eventBusSend(_eventBusURL, _address, json);            
            $('#message').val("");
        }    
    });
    
    $('#message').keypress(function (e) {
        var key = e.which;
        if(key == 13){
            if(NotEmpty(message()) == true){              
                var json = {};
                json.inputMessage = message();
                eventBusSend(_eventBusURL, _address, json);            
                $('#message').val("");
                return false;
            }    
         }
    });
       
    var message = function typeMessage(){
        var inputText = $('#message').val();
        return inputText;
    }
    
    function NotEmpty($input){    
        if($input == undefined || $input == null || $input == '' || $input.replace(/\s/g, '') == '' || $input.length == 0){                
            return false;
        }
        return true;
    }        

// Conocer la version del navegador
navigator.sayswho= (function(){
    var N = navigator.appName, ua = navigator.userAgent, tem;
	var M = ua.match(/(opera|chrome|safari|firefox|msie|trident)\/?\s*(\.?\d+(\.\d+)*)/i);
	if (M && (tem = ua.match(/version\/([\.\d]+)/i)) != null) {
		M[2] = tem[1];
	}
	M = M ?  M[1] + ' Version: ' + M[2] :  N + ', ' + navigator.appVersion  + ', ' + '-?' ;
	return M;
})();