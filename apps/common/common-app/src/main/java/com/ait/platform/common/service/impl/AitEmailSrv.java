package com.ait.platform.common.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.platform.common.model.vo.AitTaskEmailAttachedVO;
import com.ait.platform.common.model.vo.AitTaskEmailPivotVO;
import com.ait.platform.common.pdf.model.vo.AitHtmlValueVO;
import com.ait.platform.common.service.IAitEmailSrv;
import com.ait.platform.common.service.IAitParamSrv;
import com.ait.platform.common.service.IAitTaskEmailPivotSrv;
import com.ait.platform.common.util.AitStringUtils;

@Service
public class AitEmailSrv extends AitSrv implements IAitEmailSrv {

	@Autowired
	private IAitParamSrv paramSrv;


	@Autowired
	private IAitTaskEmailPivotSrv emailSrv;
	
	public void sendEmail(String emailSubject, String template, Object data, List<AitTaskEmailAttachedVO> attachedList, String... emails) {
		// se crea el body del mensaje a partir de la plantilla y del objeto a usar como fuente de datps
		String body = buildBody(template, data);
		// se envia el email a la cola
		addEmailToQueue(emailSubject, body, attachedList, emails);
	}

	private String buildBody(String param, Object data) {
		// se consulta la plantilla en AIT_PARAMS
		String template = paramSrv.getVOByName(param).getValue();

		// se sacan los valores a usar en la plantilla del objeto data
		AitHtmlValueVO values = new AitHtmlValueVO();
		AitStringUtils.getValuesFromTemplate(values, template, data);

		// se retorna el texto del mensaje decorado con la informacion de la solicitud
		return AitStringUtils.replaceContent(template, values);
	}

	private void addEmailToQueue(String subject, String body, List<AitTaskEmailAttachedVO> attachedList, String... emails) {
		// enviar email al evaluador
		AitTaskEmailPivotVO email = new AitTaskEmailPivotVO();
		email.setAttachments(new HashSet<AitTaskEmailAttachedVO>());

		// se envian los emails a los involucrados
		LinkedList<String> list = new LinkedList<>(Arrays.asList(emails));
		email.setEmailTo(list.remove(0));

		email.setEmailCC(String.join(",", list));

		email.setEmailSubject(subject);
		email.setEmailBody(body);

		// informacion de adjuntos
		email.setAttachments(new HashSet<>(attachedList));

		// se agrega el email a la cola
		emailSrv.create(email);
	}

}
