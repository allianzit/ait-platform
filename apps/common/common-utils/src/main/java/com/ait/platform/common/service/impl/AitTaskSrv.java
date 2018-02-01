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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.model.entity.AitTask;
import com.ait.platform.common.repository.IAitTaskRepo;
import com.ait.platform.common.service.IAitTaskSrv;

/**
 * @author AllianzIT
 *
 */
@Service
public class AitTaskSrv implements IAitTaskSrv {

	@Autowired
	private IAitTaskRepo taskRepository;

	@Override
	@Transactional(readOnly = true)
	public Collection<AitTask> getTasks(String serverIp, int serverPort) {
		return taskRepository.listByServer(serverIp, serverPort);
	}
	@Override
	@Transactional
	public void updaTasks(String serverIp, int serverPort) {
		taskRepository.updateByServerIpAndServerPort(serverIp, serverPort);
	}

	@Override
	@Transactional
	public void updateTaskState(AitTask task) {
		taskRepository.save(task);
	}

	@Override
	public AitTask getTaskByBeanName(String beanName) {
		return taskRepository.getByBeanName(beanName);
	}

}
