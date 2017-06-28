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
package com.ait.platform.common.model.vo;

import java.io.Serializable;

import com.ait.platform.common.model.enums.EAitListPropertyType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author AllianzIT
 *
 */
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AitListPropertyTypeVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -641731589178723878L;

	private Integer id;
	private Boolean enabled = Boolean.TRUE;
	private Boolean allowMultiple = Boolean.FALSE;
	private Boolean required = Boolean.TRUE;
	private String code;
	private String name;
	private String icon;
	private EAitListPropertyType type;
	private String mask;
	private AitListTypeVO listType;
	private AitListTypeVO validOptions;

	public AitListPropertyTypeVO(final Integer id) {
		this.id = id;
	}

}
