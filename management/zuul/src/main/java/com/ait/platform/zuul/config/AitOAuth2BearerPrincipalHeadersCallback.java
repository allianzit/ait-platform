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

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;

import com.github.mthizo247.cloud.netflix.zuul.web.socket.WebSocketHttpHeadersCallback;

/**
 * @author Marcin Podlodowski
 */
@Component
public class AitOAuth2BearerPrincipalHeadersCallback implements WebSocketHttpHeadersCallback {

	private static final Logger logger = LoggerFactory.getLogger(AitOAuth2BearerPrincipalHeadersCallback.class);

	@Override
	public WebSocketHttpHeaders getWebSocketHttpHeaders(final WebSocketSession userAgentSession) {
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		String accessToken = userAgentSession.getUri().getQuery();
		logger.debug(accessToken);
		headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
		// Principal principal = userAgentSession.getUri().HandshakeHeaders()Principal();
		// if (principal != null && OAuth2Authentication.class.isAssignableFrom(principal.getClass())) {
		// OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
		// OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
		// String accessToken = details.getTokenValue();
		// headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
		// if (logger.isDebugEnabled()) {
		// logger.debug("Added Oauth2 bearer token authentication header for user " + principal.getName() + " to web sockets http headers");
		// }
		// } else {
		// if (logger.isDebugEnabled()) {
		// logger.debug("Skipped adding basic authentication header since user session principal is null");
		// }
		// }
		return headers;
	}

}
