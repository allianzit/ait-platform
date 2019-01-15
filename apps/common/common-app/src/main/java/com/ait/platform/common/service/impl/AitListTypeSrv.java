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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.entity.AitListType;
import com.ait.platform.common.model.vo.AitListTypeVO;
import com.ait.platform.common.repository.IAitListTypeRepo;
import com.ait.platform.common.service.IAitListTypeSrv;

/**
 * @author AllianzIT
 *
 */
@Service
@Transactional
public class AitListTypeSrv extends AitSrv implements IAitListTypeSrv {

	@Autowired
	private IAitListTypeRepo listTypeRepo;

	@Override
	public List<AitListTypeVO> findAll() {
		return listTypeRepo.findAll().stream().map(opt -> convertAToB(opt, new AitListTypeVO())).collect(Collectors.toList());
	}

	@Override
	public AitListTypeVO update(AitListType listType) {
		AitListType type = listTypeRepo.findOne(listType.getId());
		if (type != null) {
			type.setName(listType.getName());
			type.setDescription(listType.getDescription());
			type.setIcon(listType.getIcon());
			listTypeRepo.save(type);
			return convertAToB(type, new AitListTypeVO());
		}
		throw new AitException(HttpStatus.BAD_REQUEST, "Tipo de lista no encontrada", "No se puede actualizar el tipo de lista");
	}
}
