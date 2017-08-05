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
import javax.persistence.FetchType;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author AllianzIT
 *
 */
@Entity
@Table(name = IAitEntity.TASK_EMAIL_ATTACHED, schema = IAitEntity.SCHEMA)
@Data 
@NoArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.TASK_EMAIL_ATTACHED + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)
public class AitTaskEmailAttached implements Serializable {

	private static final long serialVersionUID = -5457305810888737243L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	@SequenceGenerator(allocationSize = 1, name = IAitEntity.SEQ + IAitEntity.TASK_EMAIL_ATTACHED, sequenceName = IAitEntity.SEQ
			+ IAitEntity.TASK_EMAIL_ATTACHED, initialValue = IAitEntity.FIRST_SEQ_VALUE, schema = IAitEntity.SCHEMA, catalog = IAitEntity.SCHEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.TASK_EMAIL_ATTACHED)
	private Integer id;

	@JoinColumn(name = "EMAIL_ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ATTACH_TASK_EMAIL"))
	@ManyToOne(fetch = FetchType.LAZY)
	private AitTaskEmailPivot email;

	@Column(name = "SUB_FOLDER", nullable = true, columnDefinition = "VARCHAR2(512)")
	private String subFolder;

	@Column(name = "DOC_UUID", nullable = false, columnDefinition = "VARCHAR2(100)")
	private String uuid;

	public AitTaskEmailAttached(final Integer id) {
		this.id = id;
	}

}
