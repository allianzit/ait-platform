package com.ait.platform.common.file.model.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import com.ait.platform.common.file.service.IAitDocumentSrv;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Meta data of a document from an archive managed by {@link IAitDocumentSrv}.
 *
 * @author Vicky <daniel.murygin[at]gmail[dot]com>
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class AitDocumentMetadataVO extends AitDocumentVO implements Serializable {

	private static final long serialVersionUID = 3945670001877848215L;
	public static final String PROP_SUBFOLDER = "subfolder";
	public static final String PROP_UUID = "uuid";
	public static final String PROP_USER_ID = "user-id";
	public static final String PROP_FILE_NAME = "file-name";
	public static final String PROP_FILE_EXT = "file-ext";
	public static final String PROP_DOCUMENT_DATE = "document-date";

	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

	protected String subfolder;
	protected String username;

	public AitDocumentMetadataVO(final String fileName, final String subfolder, final String username) {
		this(UUID.randomUUID().toString(), fileName, subfolder, username);
	}

	public AitDocumentMetadataVO(final String uuid, final String fileName, final String subfolder, final String username) {
		super(uuid, fileName, fileName.substring(fileName.lastIndexOf('.')), new Date());
		this.subfolder = subfolder;
		this.username = username;
	}

	public AitDocumentMetadataVO(final Properties properties) {
		super(properties.getProperty(PROP_UUID), properties.getProperty(PROP_FILE_NAME), properties.getProperty(PROP_FILE_EXT), null);
		subfolder = properties.getProperty(PROP_SUBFOLDER);
		username = properties.getProperty(PROP_USER_ID);
	}

	public Properties createProperties() {
		final Properties props = new Properties();
		props.setProperty(PROP_SUBFOLDER, getSubfolder());
		props.setProperty(PROP_UUID, getUuid());
		props.setProperty(PROP_FILE_NAME, getFileName());
		props.setProperty(PROP_FILE_EXT, getFileExt());
		if (getUsername() != null) {
			props.setProperty(PROP_USER_ID, getUsername());
		}
		props.setProperty(PROP_DOCUMENT_DATE, DATE_FORMAT.format(getDocumentDate()));
		return props;
	}
}
