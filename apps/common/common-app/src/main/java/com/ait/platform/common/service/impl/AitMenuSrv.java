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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.config.IAitCache;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.entity.AitMenu;
import com.ait.platform.common.repository.IAitMenuRepo;
import com.ait.platform.common.service.IAitMenuSrv;

/**
 * @author AllianzIT
 *
 */
@Service
@Transactional
public class AitMenuSrv implements IAitMenuSrv {

	private static final Logger logger = LoggerFactory.getLogger(AitMenuSrv.class);

	@Autowired
	private IAitMenuRepo menuRepo;

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = IAitCache.MENU_ROOT_OPTS, unless = "#result.isEmpty()") // formato: sistema:nombreCache
	public List<AitMenu> findRootOpts() {
		AitLogger.debug(logger, "Searching options menu that have a path");
		return menuRepo.findRootOpts();
	}

}
