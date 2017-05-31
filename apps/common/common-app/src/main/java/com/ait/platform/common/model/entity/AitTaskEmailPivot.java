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

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.ait.platform.common.model.entity.AitTaskPivot;
import com.ait.platform.common.util.IAitEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author AllianzIT
 */

@Entity
@Table(name = IAitEntity.TASK_EMAIL_PIVOT, schema = IAitEntity.SCHEMA)
@Data
@NoArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value = IAitEntity.TASK_EMAIL_PIVOT + IAitEntity.BACKUP_SUFIX, schema = IAitEntity.SCHEMA)
public class AitTaskEmailPivot extends AitTaskPivot {

	private static final long serialVersionUID = -4934479191950402312L;

	@Id
	@Basic(optional = false)
	@Column(name = "ID")
	@SequenceGenerator(allocationSize = 1, name = IAitEntity.SEQ + IAitEntity.TASK_EMAIL_PIVOT, sequenceName = IAitEntity.SEQ
			+ IAitEntity.TASK_EMAIL_PIVOT, initialValue = IAitEntity.FIRST_SEQ_VALUE, schema = IAitEntity.SCHEMA, catalog = IAitEntity.SCHEMA)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.TASK_EMAIL_PIVOT)
	private Integer id;

	@Column(name = "EMAIL_TO", nullable = false)
	private String emailTo;

	@Column(name = "EMAIL_CC", nullable = true)
	private String emailCC;

	@Column(name = "EMAIL_SUBJECT", nullable = false)
	private String emailSubject;

	@Column(name = "BODY", columnDefinition = "CLOB NOT NULL")
	@Lob
	private String emailBody;

	@Column(name = "SENDED_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date sendedDate;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "email", cascade = CascadeType.ALL)
	private Set<AitTaskEmailAttached> attachments;
}
