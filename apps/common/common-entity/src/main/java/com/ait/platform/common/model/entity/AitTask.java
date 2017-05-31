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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ait.platform.common.util.IAitEntity;
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
@Entity
@Table(name = IAitEntity.TASK, schema = IAitEntity.SCHEMA, //
		indexes = { @Index(name = "IDX_" + IAitEntity.TASK, columnList = "BEAN_NAME") }, //
		uniqueConstraints = { @UniqueConstraint(columnNames = { "BEAN_NAME" }, name = "UK_TASK") })
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.TASK + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)
public class AitTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5226166349782167291L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	@SequenceGenerator(allocationSize = 1, name = IAitEntity.SEQ + IAitEntity.TASK, sequenceName = IAitEntity.SEQ + IAitEntity.TASK, initialValue = IAitEntity.FIRST_SEQ_VALUE, schema = IAitEntity.SCHEMA, catalog = IAitEntity.SCHEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.TASK)
	private Integer id;

	@Column(name = "IS_ENABLED", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(1)")
	private Boolean enabled = Boolean.TRUE;

	@Column(name = "BEAN_NAME", columnDefinition = "VARCHAR2(100)  not null")
	private String bean;

	@Column(name = "DESCRIPTION", nullable = true)
	private String description;

	@Column(name = "SERVER_IP", columnDefinition = "varchar2(64) not null")
	private String serverIp;

	@Column(name = "SERVER_PORT", nullable = false)
	private int serverPort;

	@Column(name = "INTERVAL_MINUTES", nullable = false)
	private Integer intervalMinutes;

	@Column(name = "MIN_HOUR", nullable = false)
	private Integer minHour = 0;

	@Column(name = "MAX_HOUR", nullable = false)
	private Integer maxHour = 24;

	@Column(name = "MAX_TRIES", nullable = false)
	private Integer maxTries;

	@Column(name = "LAST_EXEC_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastExecDate;

	@Column(name = "NEXT_EXEC_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date nextExecDate;

	@Column(name = "LAST_EXEC_MSG", nullable = false)
	private String lastExecMsg;

	@Column(name = "WAS_SUCCESSFUL", nullable = false)
	private Boolean successful;

	@Column(name = "LAST_SUCCESSFUL_DATE", nullable = true)
	private Date lastSuccessfulDate;

	@Column(name = "STOP_ON_FAIL", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(0)")
	private Boolean stopOnFail = Boolean.FALSE;

	@Column(name = "IS_RUNNING", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(0)")
	private Boolean isRunning = Boolean.FALSE;

	public AitTask(final Integer id) {
		this.id = id;
	}

}
