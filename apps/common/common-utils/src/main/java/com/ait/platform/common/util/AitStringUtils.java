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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ait.platform.common.constants.IAitConstants;
import com.ait.platform.common.logger.AitLogger;

/**
 * @author AllianzIT
 *
 */
public class AitStringUtils {

	private static final Logger logger = LoggerFactory.getLogger(AitStringUtils.class);

	private AitStringUtils() {

	}

	public static String removeSpaces(final String str) {
		return str.replaceAll("\\s", "").replaceAll("\\.", "").replaceAll("\\,", "");
	}

	public static boolean isEmpty(final Object str) {
		return str == null || str.toString().trim().isEmpty();
	}

	public static String processTemplate(final File file, final Map<String, String> parameters) {
		return processTemplate(file, IAitConstants.LOCALE, parameters);
	}

	public static String processTemplate(final File file, final Locale locale, final Map<String, String> parameters) {
		final String template = readFile(file).toString();
		return processTemplate(template, locale, parameters);
	}

	public static String processTemplate(final String template, final Map<String, String> parameters) {
		return processTemplate(template, IAitConstants.LOCALE, parameters);
	}

	public static String processTemplate(final String template, final Locale locale, final Map<String, String> parameters) {
		String ret = template;
		for (final String k : parameters.keySet()) {
			if (parameters.get(k) != null) {
				ret = ret.replaceAll(":@" + k + "@:", parameters.get(k));
			}
		}
		AitLogger.debug(logger,"Retorno processTemplate " + ret);
		return ret;
	}

	public static StringBuffer readFile(final File template) {
		BufferedReader br = null;
		final StringBuffer buf = new StringBuffer();

		try {

			String sCurrentLine;
			final InputStream inStream = new FileInputStream(template);
			br = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));

			while ((sCurrentLine = br.readLine()) != null) {
				buf.append(sCurrentLine);
			}

		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (final IOException ex) {
				ex.printStackTrace();
			}
		}
		return buf;
	}

}
