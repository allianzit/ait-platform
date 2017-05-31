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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

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

	@Autowired(required = false)
	private CacheManager cacheManager;

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

	private OAuth2Authentication getUser() {
		return (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
	}

}
