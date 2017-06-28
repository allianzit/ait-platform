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

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.ait.platform.common.config.AitEmailConfig;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.vo.AitMailVO;
import com.ait.platform.common.service.IAitMailSenderSrv;
import com.ait.platform.common.util.AitDateUtils;

/**
 * @author AllianzIT
 *
 */
@Service("mailSenderService")
@EnableConfigurationProperties(AitEmailConfig.class)
public class AitMailSenderSrv implements IAitMailSenderSrv {

	private static final Logger logger = LoggerFactory.getLogger(AitMailSenderSrv.class);

	@Autowired
	private AitEmailConfig properties;

	@Override
	public void sendMail(final AitMailVO mail) {
		if (properties.isMailSupported()) {
			AitLogger.trace(logger,"Creando hilo para el envio del email con sujeto: {}", mail.getSubject());
			new Thread(new Runnable() {
				@Override
				public void run() {
					AitLogger.trace(logger,"Iniciando ejecución de hilo para el envio del email con sujeto: {}", mail.getSubject());
					send(mail);
				}
			}).start();
		} else {
			AitLogger.error(logger,"Envio de emails no soportado");
		}
	}

	private void send(final AitMailVO mail) {
		final Session session = Session.getInstance(properties.getMessageProperties(), new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getUser(), properties.getPassword());
			}
		});

		try {
			final Message message = new MimeMessage(session);
			AitLogger.trace(logger,"creando mensaje", message);
			message.setSentDate(AitDateUtils.getCurrentDate());
			message.setFrom(new InternetAddress(properties.getFrom()));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getToUser().get(0)));
			for (int i = 1; i < mail.getToUser().size(); i++) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(mail.getToUser().get(i)));
			}

			message.setSubject(mail.getSubject());

			final MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mail.getContent(), "text/html; charset=utf-8");

			final Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (!mail.getAttachment().isEmpty()) {
				for (final File file : mail.getAttachment()) {
					if (file != null) {
						addAttachment(multipart, file);
					}
				}

			}
			message.setContent(multipart);
			Transport.send(message);

		} catch (final MessagingException e) {
			e.printStackTrace();
			AitLogger.error(logger,"Error enviando mensaje", e);
			throw new RuntimeException(e);
		} catch (final Exception e) {
			AitLogger.error(logger,"Error enviando mensaje", e);
			throw new RuntimeException(e);
		}
	}

	private static void addAttachment(final Multipart multipart, final File file) throws MessagingException {
		final MimeBodyPart messageBodyPart = new MimeBodyPart();
		final DataSource source = new FileDataSource(file);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(file.getName());
		messageBodyPart.setHeader("Content-ID", "<" + file.getName() + ">");

		multipart.addBodyPart(messageBodyPart);
	}

	public static void main(String[] args) {
		AitMailSenderSrv srv = new AitMailSenderSrv();
		srv.properties = new AitEmailConfig();
		AitMailVO dto = new AitMailVO();
		dto.addToUser("rmcruzv@gmail.com");
		dto.setContent("contenido del mensaje. <p><b>texto en <i>HTML</i></b></p>Fin");
		dto.setSubject("Titulo del mensaje");
		srv.send(dto);
	}

}
