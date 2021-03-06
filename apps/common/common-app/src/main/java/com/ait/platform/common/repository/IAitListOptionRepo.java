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

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ait.platform.common.model.entity.AitListOption;

/**
 * @author AllianzIT
 *
 */
@RepositoryRestResource(exported = false)
public interface IAitListOptionRepo extends RevisionRepository<AitListOption, Integer, Integer>, JpaRepository<AitListOption, Integer> {

	@Query(value = "SELECT o FROM AitListOption o WHERE o.listType.code=?1 and (upper(o.name) like ?2 or upper(o.internalCode) like ?2 )")
	List<AitListOption> findByListTypeAndFilter(String typeList, String filter, Pageable pageable);

	Collection<AitListOption> findByListTypeCode(String listType);

}
