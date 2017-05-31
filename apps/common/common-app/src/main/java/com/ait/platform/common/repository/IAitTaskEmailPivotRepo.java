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
 */package com.ait.platform.common.repository;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ait.platform.common.model.entity.AitTaskEmailPivot;

/**
 * @author AllianzIT
 *
 */
@RepositoryRestResource(exported = false)
public interface IAitTaskEmailPivotRepo extends RevisionRepository<AitTaskEmailPivot, Integer, Integer>, JpaRepository<AitTaskEmailPivot, Integer> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM AitTaskEmailPivot p WHERE p.state = ?1 order by p.id")
	List<AitTaskEmailPivot> listByState(final String state);

}
