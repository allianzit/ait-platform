/*
 * Copyright 2014 the original author or authors.
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
package com.ait.platform.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ait.platform.common.logger.AitLogger;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Interceptor that inject the authentication information to the request. Only works when the invocation use SEMAPHORE
 * 
 * @author AllianzIT
 *
 */
@Component
public class AitApiClientInterceptor implements RequestInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(AitApiClientInterceptor.class);

	private static final String AUTH_HEADER = "Authorization";
	private static final String BEARER = "Bearer";

	@Override
	public void apply(RequestTemplate template) {
		if (!template.headers().containsKey(AUTH_HEADER)) {
			AitLogger.debug(logger, "Trying to assign the authorization token to the request header");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getDetails() instanceof OAuth2AuthenticationDetails) {
				OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
				if (StringUtils.isEmpty(details.getTokenValue())) {
					AitLogger.warn(logger, "Empty token value.");
				} else {
					template.header(AUTH_HEADER, String.format("%s %s", BEARER, details.getTokenValue()));
				}
			} else {
				AitLogger.warn(logger, "Null or unknown authentication type object: {}", auth);
			}
		}
	}
}
