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
package com.ait.platform.common.service.impl;

import java.io.File;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ait.platform.common.constants.IAitConstants;
import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.file.service.IAitDocumentSrv;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.entity.AitTaskEmailAttached;
import com.ait.platform.common.model.entity.AitTaskEmailPivot;
import com.ait.platform.common.service.IAitMailSenderSrv;
import com.ait.platform.common.util.AitDateUtils;

/**
 * @author AllianzIT
 *
 */
@Service("mailSenderService")
public class AitMailSenderSrv implements IAitMailSenderSrv {

	private static final Logger logger = LoggerFactory.getLogger(AitMailSenderSrv.class);

	@Autowired
	private IAitDocumentSrv archiveSrv;

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendMail(final AitTaskEmailPivot mail) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			AitLogger.trace(logger, "creando mensaje", message);
			message.setSentDate(AitDateUtils.getCurrentDate());

			helper.setTo(mail.getEmailTo());

			if (!mail.getEmailCC().isEmpty()) {
				String[] ccList = mail.getEmailCC().split(",");
				helper.setCc(ccList);
			}

			helper.setSubject(mail.getEmailSubject());

			helper.setText(mail.getEmailBody(), true);

			Set<AitTaskEmailAttached> attachemens = mail.getAttachments();
			for (AitTaskEmailAttached attached : attachemens) {
				addAttachment(helper, archiveSrv.getDocumentPath(attached.getSubFolder(), attached.getUuid()));
			}

			emailSender.send(message);

		} catch (final MessagingException e) {
			e.printStackTrace();
			AitLogger.error(logger, "Error enviando mensaje", e);
			throw new RuntimeException(e);
		} catch (final Exception e) {
			AitLogger.error(logger, "Error enviando mensaje", e);
			throw new RuntimeException(e);
		}
	}

	private void addAttachment(final MimeMessageHelper helper, final String filePath) throws MessagingException {
		if (filePath != null) {
			String fileName = filePath.substring(filePath.lastIndexOf(IAitConstants.SEPARATOR) + 1);
			FileSystemResource file = new FileSystemResource(new File(filePath));
			helper.addAttachment(fileName, file);
		} else {
			throw new AitException(HttpStatus.BAD_REQUEST, "El path del archivo es nulo", "Mensaje: " + helper.getMimeMessage().getSubject());
		}
	}
}
