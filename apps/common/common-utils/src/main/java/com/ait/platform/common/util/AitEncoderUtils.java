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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ait.platform.common.logger.AitLogger;

/**
 * @author AllianzIT
 *
 */
public class AitEncoderUtils {

	private static final Logger logger = LoggerFactory.getLogger(AitDateUtils.class);

	private AitEncoderUtils() {
	}

	public static String convertMD5(final String toConvert) {
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(toConvert.getBytes());
			final StringBuffer sb = new StringBuffer();
			final byte byteData[] = md.digest();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			AitLogger.debug(logger, "Retorno convertMD5 " + sb.toString());
			return sb.toString();
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
