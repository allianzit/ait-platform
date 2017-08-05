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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.model.entity.AitTaskEmailPivot;
import com.ait.platform.common.model.vo.AitTaskVO;
import com.ait.platform.common.service.IAitMailSenderSrv;
import com.ait.platform.common.service.IAitTaskEmailPivotSrv;

/**
 * @author AllianzIT
 *
 */
@Component
public class AitSendEmailTask extends AitTaskBase {

	@Autowired
	private IAitMailSenderSrv emailSrv;

	@Autowired
	private IAitTaskEmailPivotSrv taskResultSrv;

	@Override
	protected boolean runTask(AitTaskVO task) {

		AitLogger.debug(logger, "Getting pendding emails");

		List<AitTaskEmailPivot> emails = taskResultSrv.listByState("A");
		final int totalEmails = emails.size();
		AitLogger.info(logger, "Total amount of pedding emails: {} ", totalEmails);

		try {
			if (totalEmails == 0) {
				resultMsg = "No hay emails pendientes por enviar";
			} else {
				for (final AitTaskEmailPivot email : emails) {
					try {
						email.setMessage("NA");
						// se envia el email
						emailSrv.sendMail(email);

						// se actualiza el estado del email a "PROCESADO"
						email.setState("P");
						email.setSendedDate(new Date());
					} catch (Exception e) {
						email.setMessage(e.getMessage());
						// se actualiza el estado del email a "ACTIVO" si no se han realizado todos los intentos permitidos, de lo contrario, se deja en ERROR
						email.setState(email.getTries() < task.getMaxTries() ? "A" : "E");
					}
					// se actualiza la cantidad de intentos
					email.setTries(email.getTries() + 1);
					// se actualiza el estado
					taskResultSrv.save(email);
				}
				// se guardan todos los registros en una sola transaccion
				resultMsg = "Cantidad de emails procesados correctamente: " + totalEmails;
			}
			return true;
		} catch (final AitException e1) {
			resultMsg = e1.getMessage();
		} catch (final Exception e) {
			e.printStackTrace();
			resultMsg = "Error inesperado";
		}
		return false;
	}

}