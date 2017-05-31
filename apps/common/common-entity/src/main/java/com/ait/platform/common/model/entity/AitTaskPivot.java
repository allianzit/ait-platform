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
package com.ait.platform.common.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.UpdateTimestamp;

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
@MappedSuperclass
public abstract class AitTaskPivot implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2857088595888009676L;

	@Column(name = "STATE", nullable = false, length = 1)
	private String state = "A";

	@Column(name = "MESSAGE", nullable = true, columnDefinition = "VARCHAR2(4000)")
	private String message = "NA";

	@Column(name = "TRIES", nullable = false)
	private Integer tries = 1;

	@Column(name = "LAST_EXEC_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date lastExecDate;

}
