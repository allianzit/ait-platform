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
package com.ait.platform.common.model.enums;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author AllianzIT
 *
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EAitListFeatureType {
	NONE(0, "Ninguno"), LIST(1, "Lista"), TEXT(2, "Texto"), NUMBER(3, "Número"), DATE(4, "Fecha");

	private Integer id;
	private String description;

	EAitListFeatureType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static EAitListFeatureType fromObject(Map<String, Object> data) {
		for (EAitListFeatureType e : EAitListFeatureType.values()) {
			if (e.getId().equals(data.get("id"))) {
				return e;
			}
		}
		return EAitListFeatureType.NONE;
	}

}