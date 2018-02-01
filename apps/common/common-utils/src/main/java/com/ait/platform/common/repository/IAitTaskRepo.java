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

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ait.platform.common.model.entity.AitTask;

/**
 * @author AllianzIT
 *
 */
@RepositoryRestResource(exported = false)
public interface IAitTaskRepo extends RevisionRepository<AitTask, Integer, Integer>, JpaRepository<AitTask, Integer> {

	@Query("SELECT t FROM AitTask t WHERE t.enabled = true and t.isRunning = false and t.serverIp = ?1 and t.serverPort = ?2 and CURRENT_TIMESTAMP > t.nextExecDate")
	Collection<AitTask> listByServer(String serverIp, int serverPort);
	
	@Query("SELECT t FROM AitTask t WHERE t.enabled = true and t.bean = ?1")
	AitTask getByBeanName(String beanName);

	@Modifying
	@Query("UPDATE AitTask t SET t.isRunning = false WHERE t.serverIp = ?1 and t.serverPort = ?2")
	void updateByServerIpAndServerPort(String serverIp, int serverPort);

}