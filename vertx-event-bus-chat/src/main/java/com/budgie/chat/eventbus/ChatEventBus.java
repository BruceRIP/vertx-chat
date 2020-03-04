/**
 * 
 */
package com.budgie.chat.eventbus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.budgie.chat.constants.ReactiveConstant;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * @author brucewayne
 *
 */
public class ChatEventBus extends AbstractVerticle{

	protected final Logger logger = LogManager.getLogger();
	private int port = 7770;
		
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		Router router = Router.router(vertx);					
		/**
		 * Configuration of the event bus. Any client that needs to relate to the Bus event must prefix /eventbus/  :: Ex: http://localhost:7770/eventbus 
		 */
		router.route("/eventbus/*").handler(configEventBusBridge());
		
		/**
		 * Creating HTTP server for attending request
		 */
        vertx.createHttpServer().requestHandler(router).listen(port, serverResponse->{
        	if (serverResponse.succeeded()) {
				startFuture.complete();
				logger.info("EventBus HTTP server started on http://localhost:{}", port);
			} else {
				logger.info("+++++ Error when starting server {} ", serverResponse.cause());
				startFuture.fail(serverResponse.cause());
			}
        });              
        
        vertx.eventBus().consumer(ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT).handler(handler -> {
        	logger.info("*************************************************************************************");
    		logger.info("Incomming message we received from {} address", ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT);    		    		
    		logger.info("Will send to {} address", ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT_POSTED);
    		logger.info("*************************************************************************************");
        	Object message = handler.body();
        	vertx.eventBus().publish(ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT_POSTED, message);
        });                
	}
	
	/**
	 * EventBus configuration
	 * @return
	 */
	private SockJSHandler configEventBusBridge() {
		return SockJSHandler.create(vertx)
				.bridge(new BridgeOptions()
				//Add option for actual incoming
				.addInboundPermitted(new PermittedOptions().setAddress(ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT))				
				//Add option for actual outgoing
				.addOutboundPermitted(new PermittedOptions().setAddress(ReactiveConstant.EVENTBUS_ADDRESS_CHAT_OUTPUT_POSTED))
				, this::manageBridgeEvent);         
	}		
	
	private void manageBridgeEvent(BridgeEvent event) {
		if (event.type() == BridgeEventType.SOCKET_CREATED) {
            logger.info("Socket was {} with {}", BridgeEventType.SOCKET_CREATED, event.getRawMessage());
        }else {
        	logger.info("Socket is {} with {}", event.type(), event.getRawMessage());
        }
        event.complete(true);
	}
}
