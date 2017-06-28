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
package com.ait.platform.common.model.vo.pdf;

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

	private EAitPdfOrigin origin;
	private String content;
	private boolean customHeader = false;
	private boolean customFooter = false;
	private AitPdfValueVO values = null;

	public AitPdfPageVO(EAitPdfOrigin origin, String content) {
		super();
		this.origin = origin;
		this.content = content;
	}

	public AitPdfPageVO(EAitPdfOrigin origin, String content, AitPdfValueVO values) {
		super();
		this.origin = origin;
		this.content = content;
		this.values = values;
	}

}