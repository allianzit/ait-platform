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

import java.util.EnumSet;

import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.Compression;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * @author AllianzIT
 *
 */
@Component
public class AitTomcatCustomizer implements EmbeddedServletContainerCustomizer {

	private static String mimeTypes[] = { "text/html", "text/plain", "text/xml", "text/css", "text/javascript", "application/javascript", "application/json" };

	@Value("${tomcat.ajp.port}")
	private int ajpPort;

	@Value("${tomcat.ajp.remoteauthentication}")
	private String remoteAuthentication;

	@Value("${tomcat.ajp.enabled}")
	private boolean tomcatAjpEnabled;

	@Value("${tomcat.ajp.jvmroute}")
	private String jvmRoute;

	@PostConstruct
	public void setJvmRoute() {
		// embedded tomcat uses this property to set the jvmRoute
		System.setProperty("jvmRoute", jvmRoute);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {

		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		if (tomcatAjpEnabled) {
			Connector ajpConnector = new Connector("AJP/1.3");
			ajpConnector.setPort(ajpPort);
			ajpConnector.setSecure(false);
			ajpConnector.setAllowTrace(false);
			ajpConnector.setScheme("http");
			tomcat.addAdditionalTomcatConnectors(ajpConnector);
		}

		return tomcat;
	}

	@Bean
    public FilterRegistrationBean shallowEtagHeaderFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ShallowEtagHeaderFilter());
        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        return registration;
    }
	
	@Override
	public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
		configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error404"));
		configurableEmbeddedServletContainer.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error403"));

		Compression compression = new Compression();
		compression.setEnabled(true);

		compression.setMimeTypes(mimeTypes);
		configurableEmbeddedServletContainer.setCompression(compression);
	}
}