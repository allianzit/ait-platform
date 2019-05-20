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
package com.ait.platform.common.constants;

import java.util.Locale;

/**
 * @author AllianzIT
 *
 */
public interface IAitConstants {

	static final String AIT_BASE_PACKAGE = "com.ait.platform";
	static final String AIT_JPA_PACKAGE = AIT_BASE_PACKAGE + ".**.repository";
	static final String AIT_ENTITY_PACKAGE = AIT_BASE_PACKAGE + ".**.entity";
	static final String AIT_API_CLIENTS_PACKAGE = AIT_BASE_PACKAGE + ".**.api.client";

	static final String PROD_PROFILE = "prod";
	static final Locale LOCALE = new Locale("es_CO");

	static final String IMAGE_DATA_PREFIX = "data:image/jpeg;base64,";

	static final String WILDCARD = "%";
	static final String ENCODE_INSENSITVE = "'US7ASCII'";
	static final char SEPARATOR = '/';
	static final String EMAIL_RESET_PWD = "EMAIL_RESET_PWD";
}
