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

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.vo.AitRecaptchaVO;

/**
 * @author AllianzIT
 *
 */
public abstract class AitRecaptchaSrv {
	private static final String RECAPTCHA_VERIF_URL = "https://www.google.com/recaptcha/api/siteverify";
	private static final String SECRET_KEY = "6LeS7gYTAAAAAHGldPuu6A3KSXaSwsO3mHDG2knr";

	public static AitRecaptchaVO checkReCaptcha(final String response) {
		try {
			final MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("secret", SECRET_KEY);
			map.add("response", response);
			final RestTemplate restTemplate = new RestTemplate();
			AitRecaptchaVO recaptchaResponse = restTemplate.postForObject(RECAPTCHA_VERIF_URL, map, AitRecaptchaVO.class);
			if (recaptchaResponse != null && recaptchaResponse.getSuccess()) {
				return recaptchaResponse;
			}
			// se asume que la validacion a caducado
			throw new AitException(new Exception(), "Caducó la validación de humano", "Debes volver a realizar la validación de humano debido a que la actual ha caducado", "Por favor intenta de nuevo");
		} catch (AitException e) {
			throw e;
		} catch (Exception e) {
			throw new AitException(e, "Falló la validación de humano", "No fue posible verificar si la petición fue realizada por un humano", "Esto puede deberse a que ha pasado mucho tiempo desde que se diligenció el formulario de validación",
					"Por favor intenta de nuevo");
		}
	}
}