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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.entity.AitListOption;
import com.ait.platform.common.model.vo.AitListOptionVO;
import com.ait.platform.common.repository.IAitListOptionRepo;
import com.ait.platform.common.repository.IAitListTypeRepo;
import com.ait.platform.common.service.IAitListOptionSrv;

/**
 * @author AllianzIT
 *
 */
@Service
@Transactional
public class AitListOptionSrv extends AitSrv implements IAitListOptionSrv {

	@Autowired
	private IAitListOptionRepo listOptionRepo;

	@Autowired
	private IAitListTypeRepo listTypeRepo;

	@Override
	@Transactional(readOnly = true)
	public List<AitListOptionVO> findByListTypeAndFilter(String listType, String filter, Integer maxResults) {
		return listOptionRepo.findByListTypeAndFilter(listType, filter + "%", new PageRequest(0, maxResults)).stream().map(opt -> convertAToB(opt, new AitListOptionVO())).collect(Collectors.toList());
	}

	@Override
	public List<AitListOptionVO> findAllByType(String listType) {
		return listOptionRepo.findByListTypeCode(listType).stream().map(opt -> convertAToB(opt, new AitListOptionVO())).collect(Collectors.toList());
	}

	@Override
	public AitListOptionVO save(String typeCode, AitListOption option) {
		option.setListType(listTypeRepo.findByCode(typeCode));
		option.setId(null);
		return convertAToB(listOptionRepo.save(option), new AitListOptionVO());
	}

	@Override
	public AitListOptionVO update(String typeCode, AitListOption option) {
		if (listOptionRepo.exists(option.getId())) {
			option.setListType(listTypeRepo.findByCode(typeCode));
			return convertAToB(listOptionRepo.save(option), new AitListOptionVO());
		}
		throw new AitException(HttpStatus.BAD_REQUEST, "Opción no encontrada", "No se puede actualizar la opción");
	}

}
