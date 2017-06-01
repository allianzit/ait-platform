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
package com.ait.platform.discovery;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ait.platform.discovery.conf.AitEurekaClientConverter;

import de.codecentric.boot.admin.config.EnableAdminServer;
import de.codecentric.boot.admin.discovery.EurekaServiceInstanceConverter;

/**
 * Server registration and discovery that that consums the Netflix Eureka
 * service registry.om. Created initially for RHINO System
 * 
 * @author AllianzIT
 *
 */
@Configuration
@EnableEurekaServer
@EnableEurekaClient
@EnableAutoConfiguration
@EnableAdminServer
public class AitDiscoveryServer extends SpringBootServletInitializer {

	public static void main(final String[] args) {
		new SpringApplicationBuilder(AitDiscoveryServer.class).web(true).run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(AitDiscoveryServer.class);
	}

	@Bean
	public EurekaServiceInstanceConverter serviceInstanceConverter() {
		return new AitEurekaClientConverter();
	}

}
