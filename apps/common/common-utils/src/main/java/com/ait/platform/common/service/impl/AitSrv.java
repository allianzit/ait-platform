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
package com.ait.platform.common.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.entity.AitUser;
import com.ait.platform.common.model.vo.AitBooleanVO;
import com.ait.platform.common.repository.IAitRepo;
import com.ait.platform.common.service.IAitSrv;

/**
 * @author AllianzIT
 *
 */
public class AitSrv implements IAitSrv {

	@Autowired
	private IAitRepo repository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired(required = false)
	private CacheManager cacheManager;

	@SuppressWarnings("rawtypes")
	@PostConstruct
	private void init() {
		Condition skipIds = new Condition() {
			final Set set = new HashSet();
			final Collection<?> col = new ArrayList<>();

			@Override
			public boolean applies(MappingContext context) {
				Class<?> type = context.getMapping().getLastDestinationProperty().getType();
				return !type.isInstance(set) && !type.isInstance(col);
			}
		};

		modelMapper.getConfiguration().setPropertyCondition(skipIds);
	}

	@Override
	public Boolean updateBoolean(final AitBooleanVO vo, final String schema, final String table, final String field) {
		return repository.updateBoolean(vo, schema, table, field);
	}

	@Override
	public void clearCache(final String cacheName) {
		cacheManager.getCache(cacheName).clear();
	}

	protected UsernamePasswordAuthenticationToken getAitPrincipal() {
		return (UsernamePasswordAuthenticationToken) getUser().getUserAuthentication();
	}

	protected String getUsername() {
		UsernamePasswordAuthenticationToken principal = getAitPrincipal();
		return principal.getPrincipal().toString();
	}

	protected AitUser getLoggetUser() {
		return repository.getLoggedUser(getUsername());
	}

	protected boolean hasRole(String role) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority(role));
	}

	private OAuth2Authentication getUser() {
		return (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
	}

	protected <A, B> B convertAToB(A source, B target) throws AitException {
		try {
			modelMapper.map(source, target);
			return target;
		} catch (Exception e) {
			throw new AitException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al convertir objeto", null, e);
		}
	}

}
