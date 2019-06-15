package com.ait.platform.common.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.util.StringUtils;

@Configuration
public class AitPrincipalExtractor {

	@Autowired
	private OAuth2ProtectedResourceDetails resource;

	@Bean
	@Primary
	public OAuth2RestOperations restTemplate(OAuth2ClientContext clientContext) {
		return new OAuth2RestTemplate(resource, clientContext);
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
	public PrincipalExtractor principalExtractor(final OAuth2RestOperations template) {
		return map -> {
			return map.get("preferred_username");
		};
	}
}
