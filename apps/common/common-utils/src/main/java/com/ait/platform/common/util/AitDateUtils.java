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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ait.platform.common.constants.IAitConstants;
import com.ait.platform.common.logger.AitLogger;

/**
 * @author AllianzIT
 *
 */
public final class AitDateUtils {

	private static final String FORMAT = "dd_MM_yyyy";
	public static final String DATE_HOUR_FORMAT = "dd/MM/yyyy HH:mm";
	public static final String DATE_HOUR_FILE_FORMAT = "dd_MM_yyyy_HH_mm";
	public static final String DATE_FORMAT = "dd/MM/yyyy";
	private static final Logger logger = LoggerFactory.getLogger(AitDateUtils.class);

	public static Date parseDate(final String date) throws Exception {
		AitLogger.debug(logger,"Fecha ");
		return parse(date, getSdfDate());
	}

	public static Date parseDate(final String date, final String format) {
		AitLogger.debug(logger,"Fecha ");
		return parse(date, new SimpleDateFormat(format));
	}

	public static Date parseDate(final Object date, final String format) {
		if (date == null) {
			return null;
		}
		AitLogger.debug(logger,"Fecha ");
		return parse(date.toString(), new SimpleDateFormat(format));
	}

	public static String formatDate() {
		return format(getCurrentDate(), getSdfDate());
	}

	public static String formatDate(final Date date) {
		return format(date, getSdfDate());
	}

	public static String formatDate(final Date date, final String format) {
		return format(date, new SimpleDateFormat(format));
	}

	public static Date parse(final String date, final SimpleDateFormat sdf) {
		try {
			AitLogger.debug(logger,"Fecha retorno");
			return date == null ? null : sdf.parse(date);
		} catch (final ParseException e) {
			AitLogger.error(logger,"Error fecha ", e);
			return null;
		}
	}

	public static String format(final Date date, final SimpleDateFormat sdf) {
		return date == null ? "" : sdf.format(date);
	}

	public static String formatShortDate(final Date date, final SimpleDateFormat sdf) {
		final String fecha = format(date, sdf);
		return fecha.toUpperCase().charAt(0) + fecha.substring(1, fecha.length());
	}

	public static String formatShortDate(final String date, final SimpleDateFormat sdf) throws Exception {
		return formatShortDate(parse(date, sdf), sdf);
	}

	public static GregorianCalendar getCalendar(final SimpleDateFormat sdf, final String fecha) throws Exception {
		final GregorianCalendar gc = new GregorianCalendar(IAitConstants.LOCALE);
		gc.setTime(parse(fecha, sdf));
		return gc;
	}

	private static SimpleDateFormat getSdfDate() {
		return new SimpleDateFormat(FORMAT);
	}

	/**
	 * Este método deberá ser sobreescrito cuando se desee manejar internacionalización, de manera que reciba el locale
	 *
	 * @return
	 */
	public static Date getCurrentDate() {
		return Calendar.getInstance(IAitConstants.LOCALE).getTime();
	}

	public static Calendar getCurrentCalendar() {
		return Calendar.getInstance(IAitConstants.LOCALE);
	}

	public static Double getDifferenceToday(final int hour, final Date currentStateDate) {
		final Date tmp = getCurrentDate();
		long delta = tmp.getTime() - currentStateDate.getTime();
		return (double) (delta /= 1000D * 60D * 60D);
	}
}
