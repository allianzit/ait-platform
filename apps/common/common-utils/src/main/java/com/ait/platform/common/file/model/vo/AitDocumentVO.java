package com.ait.platform.common.file.model.vo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(of = { "uuid" })
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class AitDocumentVO implements Serializable {

	private static final long serialVersionUID = 6638382645411750362L;

	protected String uuid;
	protected String fileName;
	protected String fileExt;
	protected Date documentDate;
	protected byte[] fileData;
	
	public AitDocumentVO(String uuid, String fileName, String fileExt, Date documentDate) {
		super();
		this.uuid = uuid;
		this.fileName = fileName;
		this.fileExt = fileExt;
		this.documentDate = documentDate;
	}
	
	

}
