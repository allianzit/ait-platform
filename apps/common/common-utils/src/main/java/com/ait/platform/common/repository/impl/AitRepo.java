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
package com.ait.platform.common.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.model.vo.AitBooleanVO;
import com.ait.platform.common.repository.IAitRepo;

import lombok.Data;

/**
 * @author AllianzIT
 *
 */
@Transactional
@Repository
@Data
public class AitRepo implements IAitRepo {

	@PersistenceContext
	private EntityManager entityManager;
	private final static String UPDATE_ACTIVE_QUERY = "UPDATE %s.%s SET %s = ?0 WHERE ID = ?1";

	@Override
	@Transactional
	public Boolean updateBoolean(final AitBooleanVO vo, final String schema, final String table, final String field) {
		final String queryStr = String.format(UPDATE_ACTIVE_QUERY, schema, table, field);
		final Query query = getEntityManager().createNativeQuery(queryStr);
		query.setParameter(0, vo.isData() ? 1 : 0);
		query.setParameter(1, vo.getId());
		return query.executeUpdate() == 1;
	}

}
