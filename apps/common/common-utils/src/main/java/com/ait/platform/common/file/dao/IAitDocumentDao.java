package com.ait.platform.common.file.dao;

import com.ait.platform.common.file.model.vo.AitDocumentMetadataVO;

/**
 * Data access object to insert, find and load {@link AitDocumentMetadataVO}s
 *
 * @author Vicky <daniel.murygin[at]gmail[dot]com>
 */
public interface IAitDocumentDao {

	/**
	 * Inserts a document in the data store.
	 *
	 * @param document
	 *            A Document
	 */
	void save(final AitDocumentMetadataVO document);
	
	/**
	 * Returns the document path from the data store with the given id. Returns null if no document was found.
	 * 
	 * @param subfolder:
	 *            sub folder of document
	 * @param uuid
	 *            The id of the document
	 * @return A document incl. file and meta data
	 */
	String getFilePath(final String subFolder, final String uuid);
	
	/**
	 * Returns the document from the data store with the given id. The document file and meta data is returned. Returns null if no document was found.
	 * 
	 * @param subfolder:
	 *            sub folder of document
	 * @param uuid
	 *            The id of the document
	 * @return A document incl. file and meta data
	 */
	AitDocumentMetadataVO load(final String subFolder, final String uuid);

	/**
	 * Returns the document from the data store with the given id. The document file and meta data is returned. Returns null if no document was found.
	 * 
	 * @param subfolder:
	 *            sub folder of document
	 * @param uuid
	 *            The id of the document
	 * @return A document incl. file and meta data
	 */
	AitDocumentMetadataVO load(final String subFolder, final String uuid, final boolean thumbnail);

}
