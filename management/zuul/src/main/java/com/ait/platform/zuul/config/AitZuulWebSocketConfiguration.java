/*
 * Copyright 2002-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ait.platform.zuul.config;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.DelegatingWebSocketMessageBrokerConfiguration;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.github.mthizo247.cloud.netflix.zuul.web.socket.ZuulWebSocketConfiguration;
import com.github.mthizo247.cloud.netflix.zuul.web.socket.ZuulWebSocketProperties;

/**
 * Zuul reverse proxy web socket configuration
 *
 * @author Ronald Mthombeni
 * @author Salman Noor
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(WebSocketHandler.class)
@ConditionalOnProperty(prefix = "zuul.ws", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ZuulWebSocketProperties.class)
@AutoConfigureAfter(DelegatingWebSocketMessageBrokerConfiguration.class)
public class AitZuulWebSocketConfiguration extends ZuulWebSocketConfiguration {
	@Autowired
	ZuulWebSocketProperties zuulWebSocketProperties;

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		boolean wsEnabled = false;
		for (Map.Entry<String, ZuulWebSocketProperties.WsBrokerage> entry : zuulWebSocketProperties.getBrokerages().entrySet()) {
			ZuulWebSocketProperties.WsBrokerage wsBrokerage = entry.getValue();
			if (wsBrokerage.isEnabled()) {
				this.addStompEndpoint(registry, wsBrokerage.getEndPoints());
				wsEnabled = true;
			}
		}

		if (!wsEnabled)
			this.addStompEndpoint(registry, UUID.randomUUID().toString());
	}

	private SockJsServiceRegistration addStompEndpoint(StompEndpointRegistry registry, String... endpoint) {
		return registry.addEndpoint(endpoint)
				// se sobreescribe la libreria cliente
				.setAllowedOrigins("*").withSockJS().setClientLibraryUrl("https://cdn.jsdelivr.net/sockjs/1/sockjs.min.js");
	}

}
