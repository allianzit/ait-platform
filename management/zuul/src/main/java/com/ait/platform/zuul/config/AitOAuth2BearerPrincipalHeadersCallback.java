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

	public WebSocketHttpHeaders getWebSocketHttpHeaders(final WebSocketSession userAgentSession) {
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		String accessToken = userAgentSession.getUri().getQuery();
		logger.debug(accessToken);
		headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
		return headers;
	}

	@Override
	public void applyHeaders(WebSocketSession userAgentSession, WebSocketHttpHeaders headers) {
		String accessToken = userAgentSession.getUri().getQuery();
		headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
	}

}
