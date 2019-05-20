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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.ait.platform.common.util.AitRevisionListener;
import com.ait.platform.common.util.IAitEntity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author AllianzIT
 *
 */
@Entity
@Table(name = IAitEntity.REVISION_INFO, schema = IAitEntity.SCHEMA)
@RevisionEntity(AitRevisionListener.class)
@Data
public class AitRevisionInfo implements Serializable {

	private static final long serialVersionUID = 2399517799117576787L;

	@Id
	@SequenceGenerator(name = IAitEntity.SEQ + IAitEntity.REVISION_INFO, sequenceName = IAitEntity.SEQ + IAitEntity.REVISION_INFO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = IAitEntity.SEQ + IAitEntity.REVISION_INFO)
	@RevisionNumber
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private int id;

	@Column(name = "REV_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	@RevisionTimestamp
	@Setter(AccessLevel.NONE)
	private Date date;

	@Column(name = "REV_USER_NAME")
	private String userName;
}