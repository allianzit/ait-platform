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
package com.ait.platform.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ait.platform.common.model.entity.AitParam;
import com.ait.platform.common.model.vo.AitParamVO;

/**
 * @author AllianzIT
 *
 */
@RepositoryRestResource(exported = false)
public interface IAitParamRepo extends RevisionRepository<AitParam, Integer, Integer>, JpaRepository<AitParam, Integer> {

	@Query("SELECT new com.ait.platform.common.model.vo.AitParamVO(p.id, p.name, p.value) FROM AitParam p WHERE p.name = ?1")
	AitParamVO getVOByName(String name);

}
