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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ait.platform.common.model.vo.AitEnvironmentVO;
import com.ait.platform.common.model.vo.AitParamVO;
import com.ait.platform.common.model.vo.AitUserVO;
import com.ait.platform.common.service.IAitParamSrv;
import com.ait.platform.common.service.IAitUserSrv;
import com.ait.platform.common.util.AitApiBase;

/**
 * @author AllianzIT
 *
 */
@RestController
@RequestMapping(value = "/secure/")
public class AitSecureCommonApi extends AitApiBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AitSecureCommonApi.class);

	@Value("${ait.uaaUri}")
	private String uaaUri;

	@Autowired
	private IAitParamSrv paramSrv;

	@Autowired
	private IAitUserSrv userSrv;

	@RequestMapping(value = "me", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AitUserVO> me() {
		return buildResponse(userSrv.getUserVO());
	}

	@RequestMapping(value = "portalConfig", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<AitEnvironmentVO> appConfig(HttpServletRequest request) {
		LOGGER.debug("Getting Keycloak Server Configuration");

		AitEnvironmentVO appEnvironment = new AitEnvironmentVO();

		appEnvironment.setKeyCloakUrl(uaaUri);
		LOGGER.info("Using User Authentication and Authorization URL : {}", uaaUri);

		AitParamVO redirectUri = paramSrv.getVOByName("REDIRECT_URL");
		appEnvironment.setRedirectUri(redirectUri.getValue());
		LOGGER.info("redirectUri: {}", redirectUri);

		return buildResponse(appEnvironment);
	}
}
