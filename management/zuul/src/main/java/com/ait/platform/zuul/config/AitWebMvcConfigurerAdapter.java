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

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptorAdapter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Management of the timeout on asynchronous configuration. Created initially for RHINO System
 * 
 * @author AllianzIT
 *
 */
@Configuration
public class AitWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter/* implements WebMvcConfigurer */ {

	// redirect al contenido estatico (en caso de estar integrado en el servidor)
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("/**/*").addResourceLocations("classpath:/static/").resourceChain(true).addResolver(new PathResourceResolver() {
			@Override
			protected Resource getResource(String resourcePath, Resource location) throws IOException {
				Resource requestedResource = location.createRelative(resourcePath);
				return requestedResource.exists() && requestedResource.isReadable() ? requestedResource : new ClassPathResource("/static/index.html");
			}
		});
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		// initial timeout
		configurer.setDefaultTimeout(15000);

		configurer.registerDeferredResultInterceptors(new DeferredResultProcessingInterceptorAdapter() {
			@Override
			public <T> boolean handleTimeout(NativeWebRequest req, DeferredResult<T> result) {
				return result.setErrorResult(new DiscoveryAsyncTimeoutException());
			}
		});
	}

	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public static class DiscoveryAsyncTimeoutException extends Exception {
		private static final long serialVersionUID = 6012181667166551043L;
	}
}