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

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ait.platform.common.model.vo.AitListOptionVO;
import com.ait.platform.common.model.vo.AitParamVO;
import com.ait.platform.common.model.vo.AitTaskEmailPivotVO;
import com.ait.platform.common.model.vo.AitUserVO;

/**
 * @author AllianzIT
 *
 */
@FeignClient("common")
interface IAitCommonClient {

	@RequestMapping(value = "/public/listOption/filter/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<List<AitListOptionVO>> findByListTypeAndFilter(@PathVariable("type") final String type, @RequestParam("filter") final String filter);

	@RequestMapping(value = "/internal/param/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<AitParamVO> getAitParamByName(@PathVariable("name") final String name);

	@RequestMapping(value = "/internal/email/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<Boolean> addEmailToQueue(@RequestBody AitTaskEmailPivotVO email);

	@RequestMapping(value = "/internal/user/{userId}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AitUserVO> getUserById(@PathVariable("userId") Integer userId);

	@RequestMapping(value = "/internal/user/byUsername/{username}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<AitUserVO> getUserByUsername(@PathVariable("username") String username);

}
