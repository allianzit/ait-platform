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

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ait.platform.common.logger.AitLogger;

/**
 * @author AllianzIT
 *
 */
public class AitFileUtils {

	private static final Logger logger = LoggerFactory.getLogger(AitFileUtils.class);

	public static byte[] fileToByte(final File file) {
		FileInputStream fileInputStream = null;

		final byte[] bFile = new byte[(int) file.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		} catch (final Exception e) {
			AitLogger.error(logger, "Error convirtiendo archivo de arreglo a bytes " + e);
			throw new RuntimeException(e);
		}
		return bFile;
	}
}
