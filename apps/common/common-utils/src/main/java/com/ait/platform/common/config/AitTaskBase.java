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
package com.ait.platform.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.vo.AitTaskVO;

/**
 * Super class for manage Tasks invocations
 * 
 * @author AllianzIT
 *
 */
@Component
public abstract class AitTaskBase {

	protected static final Logger logger = LoggerFactory.getLogger(AitTaskBase.class);

	protected String resultMsg = "";

	abstract protected boolean runTask(AitTaskVO task);

	public String getResultMsg() {
		AitLogger.debug(logger, "Result message: {}", resultMsg);
		return resultMsg;
	}

}