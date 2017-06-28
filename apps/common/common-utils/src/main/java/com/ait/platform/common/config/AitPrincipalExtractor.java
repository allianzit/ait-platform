package com.ait.platform.common.config;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.util.StringUtils;

@Configuration
public class AitPrincipalExtractor {

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
	public PrincipalExtractor principalExtractor(final OAuth2RestOperations template) {
		return map -> {
			return map.get("preferred_username");
		};
	}
}
