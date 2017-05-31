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
 */package com.ait.platform.zuul;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.util.StringUtils;

/**
 * @author AllianzIT
 *
 */

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
@EnableEurekaClient
@ComponentScan({ "com.ait.platform.zuul" })
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AitZuulServer extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AitZuulServer.class, args);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/index.html", "/home.html", "/", "/login", "/login/**", "/webjars/**", "**/public/**").permitAll()//
				.anyRequest().authenticated()//
				.and().exceptionHandling()//
				.and().logout().logoutSuccessUrl("/").permitAll()//
				.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}

	// se ignora todo el path asociado a contenido estatico
	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/public/**");
	}

	@Bean
	protected OAuth2RestTemplate OAuth2RestTemplate(OAuth2ProtectedResourceDetails resource, OAuth2ClientContext context) {
		return new OAuth2RestTemplate(resource, context);
	}

	@Bean
	@SuppressWarnings("unchecked")
	public AuthoritiesExtractor authoritiesExtractor(OAuth2RestOperations template) {
		return map -> {
			ArrayList<String> list = (ArrayList<String>) map.get("authorities");
			if (!list.isEmpty()) {
				return AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.collectionToCommaDelimitedString(list));
			}
			return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		};
	}

}