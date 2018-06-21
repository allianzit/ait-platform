package com.ait.platform.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Configuracion de WebSockets para manejar procesos asincronos
 * 
 * @author Roland Cruz
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class AitWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// canales a los cuales se enviaran los mensajes
		config.enableSimpleBroker("/topic", "/queue");

		// prefijo para los canales
		config.setApplicationDestinationPrefixes("/ws");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-ait").setAllowedOrigins("*").withSockJS().setClientLibraryUrl("https://cdn.jsdelivr.net/sockjs/1/sockjs.min.js");
	}

}