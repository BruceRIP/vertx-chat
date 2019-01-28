/**
 * 
 */
package com.budgie.chat;

import com.budgie.chat.eventbus.ChatEventBus;

import io.vertx.core.Vertx;

/**
 * @author brucewayne
 *
 */
public class RunVerticles {

	public static void main(String[] args) {		
		Vertx.vertx().deployVerticle(ChatEventBus.class.getName());
	}
}
