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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
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
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import com.ait.platform.zuul.config.AitOAuth2BearerPrincipalHeadersCallback;
import com.github.mthizo247.cloud.netflix.zuul.web.socket.WebSocketHttpHeadersCallback;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AllianzIT
 *
 */

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
@EnableEurekaClient
@EnableWebSocketMessageBroker
@RestController
@ComponentScan({ "com.ait.platform.zuul" })
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AitZuulServer extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(AitZuulServer.class, args);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/login", "/login/**", "**/public/**", "/ws-ait**", "/ui/**", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.map", "/assets/*").permitAll()//
				.anyRequest().authenticated()//
				.and().exceptionHandling()//
				.and().logout().logoutSuccessUrl("/").permitAll()//
				.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN));
	}

	// se ignora todo el path asociado a contenido estatico
	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("**/public/**", "/ws-ait**", "/ui/**", "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.png", "/**/*.map", "/assets/*");
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

	@Bean
	public PrincipalExtractor principalExtractor(OAuth2RestOperations template) {
		return map -> {
			return map.get("preferred_username");
		};
	}

	@Bean(name = "commonsMultipartResolver")
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean
	public WebSocketHttpHeadersCallback webSocketHttpHeadersCallback() {
		return new AitOAuth2BearerPrincipalHeadersCallback();
	}

	@RestController
	public class ZuulApi {
		@Autowired
		OAuth2RestTemplate template;

		@Data
		@AllArgsConstructor
		public class AitMessage {
			String token;
		}

		@RequestMapping("/csrf")
		public AitMessage csrf(CsrfToken token) {
			return new AitMessage(template.getAccessToken().getValue());
		}
	}
}