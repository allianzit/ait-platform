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
package com.ait.platform.zuul.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AllianzIT
 *
 */
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class AitException extends RuntimeException {

	private static final long serialVersionUID = 5170309554476956564L;

	private HttpStatus status;
	private String message;
	private List<String> errors;
	private Throwable cause;

	public AitException(final HttpStatus status, final String message, final String error) {
		this(status, message, Arrays.asList(error), new Exception());
	}

	public AitException(final HttpStatus status, final String message, final List<String> error) {
		this(status, message, error, new Exception());
	}

	public AitException(Throwable cause, final String message, final String... error) {
		this(HttpStatus.BAD_REQUEST, message, Arrays.asList(error), cause);
	}

	public String getCompleteMessage() {
		StringBuffer msg = new StringBuffer(message);
		for (String error : errors) {
			msg.append("\n");
			msg.append(error);
		}
		return msg.toString();
	}

}