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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.model.vo.AitListOptionVO;
import com.ait.platform.common.repository.IAitListOptionRepo;
import com.ait.platform.common.service.IAitListOptionSrv;

/**
 * @author AllianzIT
 *
 */
@Service
@Transactional
public class AitListOptionSrv extends AitSrv implements IAitListOptionSrv {

	// private static final Logger logger = LoggerFactory.getLogger(AitListOptionSrv.class);

	@Autowired
	private IAitListOptionRepo listOptionRepo;

	@Override
	@Transactional(readOnly = true)
	public List<AitListOptionVO> findByListTypeAndFilter(String listType, String filter, Integer maxResults) {
		return listOptionRepo.findByListTypeAndFilter(listType, filter + "%", new PageRequest(0, maxResults)).stream().map(opt -> convertAToB(opt, new AitListOptionVO())).collect(Collectors.toList());
	}

}
