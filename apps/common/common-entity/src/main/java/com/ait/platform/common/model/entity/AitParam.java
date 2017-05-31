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
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name = IAitEntity.PARAM, schema = IAitEntity.SCHEMA, //
		indexes = { @Index(name = "IDX_" + IAitEntity.PARAM, columnList = "NAME") }, //
		uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME" }, name = "UK_" + IAitEntity.PARAM) })
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.PARAM + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)
public class AitParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6616202590421310403L;

	@Id
	@Column
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.PARAM)
	@SequenceGenerator(//
			catalog = IAitEntity.SCHEMA, //
			schema = IAitEntity.SCHEMA, //
			name = IAitEntity.SEQ + IAitEntity.PARAM, //
			sequenceName = IAitEntity.SEQ + IAitEntity.PARAM, //
			initialValue = IAitEntity.FIRST_SEQ_VALUE, //
			allocationSize = 1 //
	)
	private Integer id;

	@Column(name = "NAME", nullable = false, length = 30)
	private String name;

	@Basic(optional = false)
	@Column(name = "VAR_VALUE", columnDefinition = "VARCHAR2(4000)")
	private String value;

	public AitParam(final Integer id) {
		this.id = id;
	}

}
