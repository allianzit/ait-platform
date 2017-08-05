package com.ait.platform.common.file.service.impl;

import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ait.platform.common.constants.IAitConstants;
import com.ait.platform.common.file.dao.IAitDocumentDao;
import com.ait.platform.common.file.model.vo.AitDocumentMetadataVO;
import com.ait.platform.common.file.model.vo.AitDocumentVO;
import com.ait.platform.common.file.service.IAitDocumentSrv;
import com.ait.platform.common.logger.AitLogger;
import com.ait.platform.common.util.AitStringUtils;

/**
 * Servicio encargador de guardar y consultar archivos tanto publicos como privados
 *
 * @author Vicky <daniel.murygin[at]gmail[dot]com>,
 * @author Adapatado por Roland Cruz
 */
@Service("aitArchiveSrv")
public class AitDocumentSrv implements IAitDocumentSrv, Serializable {

	private static final long serialVersionUID = 8119784722798361327L;

	private static final Logger logger = LoggerFactory.getLogger(AitDocumentSrv.class);

	@Autowired
	private IAitDocumentDao documentDao;

	/**
	 * Guarda un documento y retorna la informacion generada del mismo (metadata)
	 *
	 * @see IAitDocumentSrv.gov.fgn.susi.core.common.file.service.ICoreArchiveSrv#save(AitDocumentMetadataVO.gov.fgn.core.common.model.dto.CoreDocumentDto)
	 *
	 */
	@Override
	public AitDocumentMetadataVO save(final AitDocumentMetadataVO document) {
		documentDao.save(document);
		AitLogger.debug(logger, "Documento guardado y metadata" + document);
		return document;
	}

	@Override
	public String getDocumentPath(final String subFolder, final String uuid) {
		return documentDao.getFilePath(subFolder, uuid);
	}

	@Override
	public AitDocumentVO getDocumentFile(final String subFolder, final String uuid) {
		return this.getDocumentFile(subFolder, uuid, false);
	}

	@Override
	public AitDocumentVO getDocumentFile(final String subFolder, final String uuid, final boolean thumbnail) {
		if (AitStringUtils.isEmpty(uuid)) {
			return new AitDocumentVO();
		}
		final AitDocumentMetadataVO document = documentDao.load(subFolder, uuid, thumbnail);
		if (document != null) {
			return (AitDocumentVO) document;
		}
		AitLogger.debug(logger, "informacion de un documento con base en su uuid " + uuid);
		return new AitDocumentVO();
	}

	@Override
	public String getImageAsString(final String subFolder, final String uuid, final boolean thumbnail) {
		if (!AitStringUtils.isEmpty(uuid)) {
			final AitDocumentMetadataVO document = documentDao.load(subFolder, uuid, thumbnail);
			if (document != null) {
				return IAitConstants.IMAGE_DATA_PREFIX + Base64.encodeBase64String(document.getFileData());
			}
		}
		return null;

	}

}
