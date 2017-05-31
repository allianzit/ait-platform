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

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Predicate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;

/**
 * API documentantion support. Works only on development environments
 * 
 * @author AllianzIT
 *
 */
@Configuration
@EnableWebMvc
@Component
@Profile("dev")
class AitApiConfig extends WebMvcConfigurerAdapter implements ApplicationContextAware {

	@Override
	public void setApplicationContext(final ApplicationContext ac) throws BeansException {
		ac.getBean("requestMappingHandlerAdapter");
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Configuration
	protected static class SwaggerConfig {

		public SwaggerConfig() {
			super();
		}

		@Bean
		public Docket api() {
			return new Docket(DocumentationType.SWAGGER_2)//
					.select()//
					.apis(RequestHandlerSelectors.any())//
					.paths(paths())// solo se documentan los metodos publicos y seguros de susi
					.build()//
					.apiInfo(apiInfo());
		}

		@Bean
		SecurityConfiguration security() {
			return new SecurityConfiguration("", "", "", "", "apiKey", ApiKeyVehicle.HEADER, "X-XSRF-TOKEN", ",");
		}

		private Predicate<String> paths() {
			return or(regex("/public.*"), regex("/secure.*"));
		}

		private ApiInfo apiInfo() {
			final ApiInfo apiInfo = new ApiInfo("AIT PLATFORM Rest API", //
					"Rest API documentation of Platform microservices.", //
					"1.0", //
					"Terms of service", //
					new Contact("Informática", "", "info@allianzit.co"), //
					"Apache License 2.0", //
					"URL: (https://www.apache.org/licenses/LICENSE-2.0)");//
			return apiInfo;
		}
	}
}
