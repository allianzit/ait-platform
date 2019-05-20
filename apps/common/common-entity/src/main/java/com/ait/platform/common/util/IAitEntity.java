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

/**
 * @author AllianzIT
 *
 */
public interface IAitEntity {

	/**
	 * Schema name
	 */
	public final static String SCHEMA = "RTEPRO";
//	public final static String SCHEMA = "C##RTE";

	/**
	 * Tables prefix
	 */
	public final static String PREFIX = "AIT_";

	/**
	 * Backup tables sufix
	 */
	public final static String BACKUP_SUFIX = "_BK";

	/**
	 * Sequences prefix
	 */
	public final static String SEQ = SCHEMA + ".SQ_";

	// Tables
	public final static String REVISION_INFO = PREFIX + "REVISION_INFO";
	public final static String USER = PREFIX + "USER";
	public final static String USER_ATTIBUTE = PREFIX + "USER_ATTIBUTE";
	public final static String PARAM = PREFIX + "PARAM";
	public final static String MENU = PREFIX + "MENU";
	public final static String LIST_TYPE = PREFIX + "LIST_TYPE";
	public final static String LIST_PROP_TYPE = PREFIX + "LIST_PROP_TYPE";
	public final static String LIST_OPTION = PREFIX + "LIST_OPTION";
	public final static String LIST_OPTION_PROP = PREFIX + "LIST_OPTION_PROP";
	public final static String TASK = PREFIX + "TASK";
	public final static String TASK_PROFILE_PIVOT = TASK + "_PROFILE_PIVOT";
	public final static String TASK_EMAIL_PIVOT = TASK + "_EMAIL_PIVOT";
	public final static String TASK_EMAIL_ATTACHED = TASK + "_EMAIL_ATTACHED";

	public static final int FIRST_SEQ_VALUE = 1000000;

}
