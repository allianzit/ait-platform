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
package com.ait.platform.common.logger;

import org.slf4j.Logger;

/**
 * @author AllianzIT
 *
 */
public class AitLogger {

	/**
	 * trace
	 * @param logger
	 * @param message
	 * @param params
	 */
	
	public static void trace(Logger logger, String message, Object... params) {

		if (logger.isTraceEnabled()) {
			logger.trace(message, params);
		}
	}
	
	/**
	 * debug
	 * @param logger
	 * @param message
	 * @param params
	 */
	
	public static void debug(Logger logger, String message, Object... params) {
		
		if (logger.isDebugEnabled()) {
			logger.debug(message, params);
		}
	}

	/**
	 * info
	 * @param logger
	 * @param message
	 * @param params
	 */
	
	public static void info(Logger logger, String message, Object... params) {
		
		if (logger.isInfoEnabled()) {
			logger.info(message, params);
		}
	}
	
	/**
	 * warn
	 * @param logger
	 * @param message
	 * @param params
	 */
	
	public static void warn(Logger logger, String message, Object... params) {
		
		if (logger.isWarnEnabled()) {
			logger.warn(message, params);
		}
	}
	
	/**
	 * error
	 * @param logger
	 * @param message
	 * @param params
	 */

	public static void error(Logger logger, String message, Object... params) {
		
		if (logger.isErrorEnabled()) {
			logger.error(message, params);
		}
	}
}
