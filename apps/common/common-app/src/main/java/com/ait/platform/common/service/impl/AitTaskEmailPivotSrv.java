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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.entity.AitTaskEmailAttached;
import com.ait.platform.common.model.entity.AitTaskEmailPivot;
import com.ait.platform.common.model.vo.AitTaskEmailPivotVO;
import com.ait.platform.common.repository.IAitTaskEmailPivotRepo;
import com.ait.platform.common.service.IAitTaskEmailPivotSrv;

/**
 * @author AllianzIT
 *
 */
@Service
public class AitTaskEmailPivotSrv extends AitTaskPivotSrv<AitTaskEmailPivot> implements IAitTaskEmailPivotSrv {

	private static final Logger logger = LoggerFactory.getLogger(AitTaskEmailPivotSrv.class);

	@Autowired
	private IAitTaskEmailPivotRepo emailRepo;

	@Override
	public Boolean create(AitTaskEmailPivotVO dto) {
		AitTaskEmailPivot email = new AitTaskEmailPivot();
		// BeanUtils.copyProperties(dto, email, "attachements");
		BeanUtils.copyProperties(dto, email);
		if (email.getAttachments() != null) {
			for (AitTaskEmailAttached attached : email.getAttachments()) {
				attached.setEmail(email);
			}
		}
		emailRepo.save(email);
		return true;
	}

	@Override
	public Boolean save(AitTaskEmailPivot email) {
		emailRepo.save(email);
		return true;
	}

	@Override
	public Boolean saveAll(List<AitTaskEmailPivot> emailList) {
		throw new AitException(new UnsupportedOperationException(), "Operacion no soportada para la tabla");
	}

	@Override
	@Transactional
	public List<AitTaskEmailPivot> listByState(final String state) {
		final String newState = "R";
		List<AitTaskEmailPivot> list = emailRepo.listByState(state);
		AitLogger.info(logger, "Reservando {} registros...", list.size());
		for (int i = 0; i < list.size(); i++) {
			if (i % batchSize == 0) {
				emailRepo.flush();
			}
			list.get(i).setState(newState);
			emailRepo.save(list.get(i));
		}
		emailRepo.flush();
		AitLogger.info(logger, "Registros reservados correctamente: {}", list.size());
		return list;
	}

}
