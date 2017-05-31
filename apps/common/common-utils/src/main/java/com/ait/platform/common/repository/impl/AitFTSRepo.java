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

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.ait.platform.common.repository.IAitFTSRepo;

/**
 * @author AllianzIT
 *
 */
@Repository
public class AitFTSRepo implements IAitFTSRepo {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public FullTextQuery getFTSQuery(String filterText, final Class<?> entityType, Integer maxResults, String... fields) {

		// entityManager para busquedas de tipo FTS
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

		// se crea el query usando Hibernate Search query DSL
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entityType).get();

		// se crea el query sobre los campos indicados
		final Query query = queryBuilder.keyword().onFields(fields).matching(filterText.trim()).createQuery();

		// se enmascara el query de Lucene en uno de Hibernate
		final FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, entityType);

		// se define la cantidad maxima de resultados si es mayor a cero
		if (maxResults > 0) {
			jpaQuery.setMaxResults(maxResults);
		}
		// se retorna el query listo para ejecución o para inyeccion de criterias
		return jpaQuery;
	}

	@Override
	public FullTextQuery getFTSWildcardQuery(String filterText, final Class<?> entityType, Integer maxResults, String field) {

		// entityManager para busquedas de tipo FTS
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

		// se crea el query usando Hibernate Search query DSL
		final QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entityType).get();

		// se crea el query sobre los campos indicados
		final Query query = queryBuilder.phrase().onField(field).sentence(filterText.trim()).createQuery();

		// se enmascara el query de Lucene en uno de Hibernate
		final FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, entityType);

		// se define la cantidad maxima de resultados si es mayor a cero
		if (maxResults > 0) {
			jpaQuery.setMaxResults(maxResults);
		}
		// se retorna el query listo para ejecución o para inyeccion de criterias
		return jpaQuery;
	}

}