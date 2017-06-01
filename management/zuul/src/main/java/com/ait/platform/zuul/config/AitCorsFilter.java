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
package com.ait.platform.zuul.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filtro para manejar y permitir peticiones externas al servidor. Esto permite la negociaciÃ³n CORS (Common Origin Resource Sharing)
 *
 * @author AllianzIT
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AitCorsFilter implements Filter {

	@Value("${spring.profiles.active}")
	private String actualProfile;

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse res, final FilterChain chain) throws IOException, ServletException {
		final HttpServletResponse response = (HttpServletResponse) res;
		String uri = ((HttpServletRequest) request).getRequestURL().toString();

		// static content cache
		if (!uri.endsWith(".css") && (uri.endsWith(".js") || uri.endsWith(".png"))) {
			response.setHeader("Cache-Control", "public, max-age=31536000");
		} else {
			response.setHeader("Cache-Control", "no-cache, no-store");
		}
		response.setHeader("Access-Control-Allow-Origin", "*");// TODO cambiar para permitir unicamente peticiones de ips conocidas y seguras
		if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) request).getMethod())) {
			response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE");
			response.setHeader("Access-Control-Allow-Headers", "X-XSRF-TOKEN, Origin, Accept, x-auth-token, X-Requested-With, Content-Type, Authorization, Access-Control-Request-Method, Access-Control-Request-Headers, Cache-Control, Content-Length");
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			chain.doFilter(request, res);
		}
	}

	@Override
	public void init(final FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

}