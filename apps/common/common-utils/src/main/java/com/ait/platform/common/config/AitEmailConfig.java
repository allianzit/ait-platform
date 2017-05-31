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
package com.ait.platform.common.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.ait.platform.common.logger.AitLogger;

/**
 * Email configuration
 * 
 * @author AllianzIT
 *
 */
@ConfigurationProperties
public class AitEmailConfig {

	private static final Logger logger = LoggerFactory.getLogger(AitEmailConfig.class);

	private static final String ERROR_MSG = "Funcionalidad de envio de correos electronicos no configurada";

	@Value("${ait.platform.mail.smtp.auth:}")
	private String smtpAuth;
	@Value("${ait.platform.mail.smtp.starttls.enable:}")
	private String smtpTtlsEnable;
	@Value("${ait.platform.mail.smtp.host:}")
	private String smtpHost;
	@Value("${ait.platform.mail.smtp.port:}")
	private String smtpPort;
	@Value("${ait.platform.mail.smtp.debug:}")
	private String debug;
	@Value("${ait.platform.mail.smtp.debug.auth:}")
	private String debugAuth;
	@Value("${ait.platform.mail.user:}")
	private String user;
	@Value("${ait.platform.mail.password:}")
	private String password;
	@Value("${ait.platform.mail.from:}")
	private String from;
	private Properties messageProperties;

	public AitEmailConfig() {
	}

	public Properties getMessageProperties() {
		if (messageProperties == null || messageProperties.isEmpty()) {
			AitLogger.debug(logger, "Cargando propiedades de envio de emails");
			messageProperties = new Properties();
			messageProperties.put("mail.smtp.auth", smtpAuth);
			messageProperties.put("mail.smtp.starttls.enable", smtpTtlsEnable);
			messageProperties.put("mail.smtp.host", smtpHost);
			messageProperties.put("mail.smtp.port", smtpPort);
			messageProperties.put("mail.smtp.debug", debug);
			messageProperties.put("mail.smtp.debug.auth", debugAuth);
			messageProperties.put("mail.debug", debug);
			messageProperties.put("mail.debug.auth", debugAuth);
			AitLogger.debug(logger, "Propiedades de email cargadas: {}", messageProperties.toString());
		}
		return messageProperties;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getFrom() {
		return from;
	}

	public boolean isMailSupported() {
		if (from != null && from.length() > 0) {
			return true;
		}
		AitLogger.error(logger, ERROR_MSG);
		throw new RuntimeException(ERROR_MSG);
	}

}
