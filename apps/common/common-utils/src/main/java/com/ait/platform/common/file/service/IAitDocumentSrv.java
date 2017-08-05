package com.ait.platform.common.file.service;

import com.ait.platform.common.file.model.vo.AitDocumentMetadataVO;
import com.ait.platform.common.file.model.vo.AitDocumentVO;

/**
 * Servicio encargador de guardar y consultar archivos tanto publicos como privados
 *
 * @author Vicky <daniel.murygin[at]gmail[dot]com>,
 * @author Adapatado por Roland Cruz
 */
public interface IAitDocumentSrv {

	/**
	 * Guarda un documento y retorna la informacion generada del mismo (metadata)
	 * 
	 * @param document
	 *            El documento a guardar
	 * @return DocumentMetadata El meta data del documento guardado
	 */
	AitDocumentMetadataVO save(final AitDocumentMetadataVO document);

	/**
	 * Retorna la informacion de un documento con base en su uuid
	 *
	 * @param subfolder:
	 *            path de carpeta(s) internas en donde esta el documento El identificador del documento
	 * @param uuid
	 *            El identificador del documento
	 * @return EL documento junto con informacion basica del mismo
	 */
	AitDocumentVO getDocumentFile(final String subFolder, final String uuid);

	/**
	 * Retorna la informacion de un documento con base en su uuid
	 *
	 * @param subfolder:
	 *            path de carpeta(s) internas en donde esta el documento El identificador del documento
	 * @param uuid
	 *            El identificador del documento
	 * @param thumbnail
	 *            Indica si se require la imagen en miniatura
	 * @return EL documento junto con informacion basica del mismo
	 */
	AitDocumentVO getDocumentFile(final String subFolder, final String uuid, final boolean thumbnail);

	/**
	 * Retorna una imagen como arreglo de bits para facilitar su pintado en pantalla
	 *
	 * @param subfolder:
	 *            path de carpeta(s) internas en donde esta el documento El identificador del documento
	 * @param uuid
	 *            El identificador de la imagen
	 * @param thumbnail
	 *            Indica si se require la imagen en miniatura
	 * @return El string que representa la imagen
	 */
	String getImageAsString(final String subFolder, final String uuid, final boolean thumbnail);

	/**
	 * Retorna la ruta de un documento con base en su uuid
	 *
	 * @param subfolder:
	 *            path de carpeta(s) internas en donde esta el documento El identificador del documento
	 * @param uuid
	 *            El identificador del documento
	 * @return EL documento junto con informacion basica del mismo
	 */
	String getDocumentPath(String subFolder, String uuid);

}
