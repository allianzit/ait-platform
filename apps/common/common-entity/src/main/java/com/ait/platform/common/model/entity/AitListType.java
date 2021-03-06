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
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = IAitEntity.LIST_TYPE, schema = IAitEntity.SCHEMA)
@NoArgsConstructor(onConstructor = @__({ @JsonCreator }))
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.LIST_TYPE + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)
public class AitListType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -641731589178723878L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	@SequenceGenerator(allocationSize = 1, name = IAitEntity.SEQ + IAitEntity.LIST_TYPE, sequenceName = IAitEntity.SEQ + IAitEntity.LIST_TYPE, initialValue = IAitEntity.FIRST_SEQ_VALUE, schema = IAitEntity.SCHEMA, catalog = IAitEntity.SCHEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.LIST_TYPE)
	private Integer id;

	@Column(name = "IS_ENABLED", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(1)")
	private Boolean enabled = Boolean.TRUE;

	@Column(name = "IS_EDITABLE", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(1)")
	private Boolean editable = Boolean.TRUE;

	@Column(name = "ALLOW_MULTIPLE", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(0)")
	private Boolean allowMultiple = Boolean.FALSE;

	@Column(name = "CODE", nullable = false, columnDefinition = "VARCHAR2(50)")
	private String code;

	@Column(name = "NAME", columnDefinition = "VARCHAR2(128)")
	private String name;

	@Column(name = "DESCRIPTION", columnDefinition = "VARCHAR2(512)")
	private String description;

	@Column(name = "ICON", nullable = true)
	private String icon;

	@Column(name = "VALUES_BY_REF", nullable = false, columnDefinition = "NUMBER (1) DEFAULT(0)")
	private Boolean valuesByReference = Boolean.FALSE;

	@JoinColumn(name = "PARENT_ID", nullable = true, referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_PARENT_TYPE"))
	@ManyToOne
	private AitListType parent;

	public AitListType(final Integer id) {
		this.id = id;
	}

}
