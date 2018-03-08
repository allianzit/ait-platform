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
package com.ait.platform.common.pdf.model.vo;

import java.io.Serializable;

import com.ait.platform.common.model.enums.pdf.EAitPdfOrigin;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author AllianzIT
 *
 */
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AitPdfPageVO implements Serializable {

	private static final long serialVersionUID = 7182235863687838588L;

	// indica si la pagina corresponde a la cabecera
	private boolean header = false;

	// indica si la pagina corresponde a pie de pagina
	private boolean footer = false;

	// origen de la plantilla a usar: archivo, url o string
	private EAitPdfOrigin origin;

	// contenido como tal
	private String content;

	// valores a reemplazar en la plantilla
	private AitHtmlValueVO values = new AitHtmlValueVO();

	public AitPdfPageVO(EAitPdfOrigin origin, String content, AitHtmlValueVO values) {
		super();
		this.origin = origin;
		this.content = content;
		this.values = values;
	}

}