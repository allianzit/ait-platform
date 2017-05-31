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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = IAitEntity.USER, schema = IAitEntity.SCHEMA)
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.USER + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)

public class AitUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4059318725676374513L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	@SequenceGenerator(allocationSize = 1, name = IAitEntity.SEQ + IAitEntity.USER, sequenceName = IAitEntity.SEQ + IAitEntity.USER, initialValue = IAitEntity.FIRST_SEQ_VALUE, schema = IAitEntity.SCHEMA, catalog = IAitEntity.SCHEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.USER)
	private Integer id;

	@Column(name = "IS_ENABLED", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(1)")
	private Boolean enabled = Boolean.TRUE;

	@Column(name = "USERNAME", unique = true, nullable = false, columnDefinition = "VARCHAR2(80)")
	private String username;

	@Column(name = "FIRSTNAME", nullable = false, columnDefinition = "VARCHAR2(80)")
	private String firstName;

	@Column(name = "LASTTNAME", nullable = false, columnDefinition = "VARCHAR2(80)")
	private String lastName;

	@Column(name = "EMAIL", columnDefinition = "VARCHAR2(80)")
	private String email;
}