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
package com.ait.platform.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.vo.AitMessageVO;

/**
 * Base class to be used by classes that generates http responses
 * 
 * @author AllianzIT
 *
 */
public abstract class AitApiBase {

	private static final Logger logger = LoggerFactory.getLogger(AitFileUtils.class);

	protected <T> ResponseEntity<T> buildResponse(T result) {
		AitLogger.trace(logger, "Construyendo respuesta a partir deobjeto: {}", result == null ? null : result.getClass().getSimpleName());
		return ResponseEntity.ok(result);
	}

	protected ResponseEntity<AitMessageVO> buildImageResponse(String imageString) {
		AitLogger.trace(logger, "Construyendo la respuesta a partir de una imagen de tamaño: {}", imageString == null ? null : imageString.length());
		return buildMessageResponse(imageString, null);
	}

	protected ResponseEntity<AitMessageVO> buildMessageResponse(String message, String id) {
		AitLogger.trace(logger, "Construyendo la respuesta de mensaje a partir de: {}. Con codigo de respuesta: {}", message, id);
		return ResponseEntity.ok(new AitMessageVO(id != null ? id : "", message));
	}

}
