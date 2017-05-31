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
package com.ait.platform.common.api.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ait.platform.common.api.client.IAitCommonRetriever;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.vo.AitParamVO;
import com.ait.platform.common.model.vo.AitTaskEmailPivotVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * @author AllianzIT
 *
 */
@Component
public class AitCommonRetriever implements IAitCommonRetriever {

	private static final Logger logger = LoggerFactory.getLogger(AitCommonRetriever.class);
	@Autowired
	private IAitCommonClient client;

	@Override
	@HystrixCommand(fallbackMethod = "errorOnGetAitParamByName", commandProperties = { @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE") })
	public AitParamVO getAitParamByName(final String name) {
		AitLogger.debug(logger, "Searching platform parameter by name: {}", name);
		try {
			return client.getAitParamByName(name).getBody();
		} catch (final Exception e) {
			e.printStackTrace();
			return errorOnGetAitParamByName(name);
		}
	}

	@Override
	@HystrixCommand(fallbackMethod = "errorOnAddEmailToQueue")
	public Boolean addEmailToQueue(final AitTaskEmailPivotVO email) {
		AitLogger.debug(logger, "Trying to add email to the Queue: {}", email.getEmailTo());
		try {
			return client.addEmailToQueue(email).getBody();
		} catch (final Exception e) {
			e.printStackTrace();
			return errorOnAddEmailToQueue(email);
		}
	}

	/********************** On feign invocation error **************************/

	public AitParamVO errorOnGetAitParamByName(String name) {
		AitLogger.debug(logger, "Error trying to get the parameter by name: {}", name);
		return new AitParamVO();
	}

	public Boolean errorOnAddEmailToQueue(AitTaskEmailPivotVO email) {
		AitLogger.debug(logger, "Error trying to add the email to the Queue: {}", email);
		return false;
	}

}
