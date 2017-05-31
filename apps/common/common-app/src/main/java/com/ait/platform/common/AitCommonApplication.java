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
package com.ait.platform.common;

import static com.ait.platform.common.constants.IAitConstants.AIT_API_CLIENTS_PACKAGE;
import static com.ait.platform.common.constants.IAitConstants.AIT_BASE_PACKAGE;
import static com.ait.platform.common.constants.IAitConstants.AIT_ENTITY_PACKAGE;
import static com.ait.platform.common.constants.IAitConstants.AIT_JPA_PACKAGE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author AllianzIT
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = { JestAutoConfiguration.class })
@EnableEurekaClient
@EnableHystrix
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
@EnableScheduling // soporte para tareas programadas
@ComponentScan({ AIT_BASE_PACKAGE })
@EntityScan({ AIT_ENTITY_PACKAGE })
@EnableJpaRepositories(basePackages = { AIT_JPA_PACKAGE }, repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@EnableFeignClients({ AIT_API_CLIENTS_PACKAGE })
public class AitCommonApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AitCommonApplication.class);
	}

	public static void main(final String[] args) {
		SpringApplication.run(AitCommonApplication.class, args);
	}

}
