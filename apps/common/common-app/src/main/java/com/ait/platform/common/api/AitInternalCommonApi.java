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
package com.ait.platform.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.vo.AitParamVO;
import com.ait.platform.common.model.vo.AitTaskEmailPivotVO;
import com.ait.platform.common.model.vo.AitUserVO;
import com.ait.platform.common.service.IAitParamSrv;
import com.ait.platform.common.service.IAitTaskEmailPivotSrv;
import com.ait.platform.common.service.IAitUserSrv;
import com.ait.platform.common.util.AitApiBase;

/**
 * @author AllianzIT
 *
 */
@RestController
@RequestMapping(value = "/internal/")
public class AitInternalCommonApi extends AitApiBase {

	private static final Logger logger = LoggerFactory.getLogger(AitInternalCommonApi.class);

	@Autowired
	private IAitParamSrv paramSrv;

	@Autowired
	private IAitUserSrv userSrv;

	@Autowired
	private IAitTaskEmailPivotSrv emailSrv;

	@RequestMapping(value = "email/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> addEmailToQueue(@RequestBody final AitTaskEmailPivotVO email) {
		AitLogger.debug(logger, "Adding email. TO: {}, subject: {}", email.getEmailTo(), email.getEmailSubject());
		return buildResponse(emailSrv.create(email));
	}

	@RequestMapping(value = "param/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AitParamVO> getParamByName(@PathVariable final String name) {
		AitLogger.debug(logger, "Searching parameter by name: {}", name);
		return buildResponse(paramSrv.getVOByName(name));
	}

	@RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<AitUserVO> getUserById(@PathVariable Integer userId) {
		return buildResponse(userSrv.getById(userId));
	}
	
	@RequestMapping(value = "user/byUsername/{username}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<AitUserVO> getUserById(@PathVariable String username) {
		return buildResponse(userSrv.getByUsername(username));
	}
}
