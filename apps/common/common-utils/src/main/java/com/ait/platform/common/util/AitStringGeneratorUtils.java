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

import java.util.Random;

/**
 * @author AllianzIT
 *
 */
public class AitStringGeneratorUtils {

	private static final char[] symbols;
	private static final char[] specials;
	private static final Random random;

	static {
		final StringBuilder tmp = new StringBuilder();
		specials = "!@#".toCharArray();
		tmp.append(specials);
		for (char ch = '1'; ch <= '9'; ++ch) {
			tmp.append(ch);
		}
		for (char ch = 'a'; ch <= 'z'; ++ch) {
			if (ch != 'o') {
				tmp.append(ch);
			}
		}
		for (char ch = 'A'; ch <= 'Z'; ++ch) {
			if (ch != 'O') {
				tmp.append(ch);
			}
		}
		symbols = tmp.toString().toCharArray();
		random = new Random();
	}

	private AitStringGeneratorUtils() {
	}

	public static String nextString(final int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}
		final char[] buf = new char[length];

		for (int idx = 0; idx < buf.length; ++idx) {
			buf[idx] = symbols[random.nextInt(symbols.length)];
		}

		return new String(buf);
	}

	public static String nextPassword(final int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}
		final char[] buf = new char[length];
		while (!verifyGoodPassword(buf)) {
			for (int idx = 0; idx < buf.length; ++idx) {
				final int nextInt = random.nextInt(symbols.length);
				buf[idx] = symbols[nextInt];
			}
		}
		return String.valueOf(buf);
	}

	private static boolean verifyGoodPassword(final char[] buf) {
		boolean hasLower = false;
		boolean hasUpper = false;
		boolean hasNumber = false;
		boolean hasSpecial = false;
		for (final char c : buf) {
			if (Character.isDigit(c)) {
				hasNumber = true;
			} else if (Character.isUpperCase(c)) {
				hasUpper = true;
			} else if (Character.isLowerCase(c)) {
				hasLower = true;
			} else if (specials.toString().contains("" + c)) {
				hasSpecial = true;
			}
		}
		return hasLower && hasUpper && hasNumber && hasSpecial;
	}
}