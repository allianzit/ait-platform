package com.ait.platform.common.file.dao.impl;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ait.platform.common.constants.IAitConstants;
import com.ait.platform.common.file.dao.IAitDocumentDao;
import com.ait.platform.common.file.model.vo.AitDocumentMetadataVO;
import com.ait.platform.common.logger.AitLogger;

/**
 * Data access object to insert, find and load {@link AitDocumentMetadataVO}s.
 *
 * FileSystemDocumentDao saves documents in the file system. No database in involved. For each document a folder is created. The folder contains the document and a properties files with the meta data of the document. Each document in the archive has
 * a Universally Unique Identifier (UUID). The name of the documents folder is the UUID of the document.
 *
 * @author AllianzIT <daniel.murygin[at]gmail[dot]com>
 */
@Service
public class AitDocumentDao implements IAitDocumentDao {

	private static final Logger logger = LoggerFactory.getLogger(AitDocumentDao.class);

	@Value("${ait.docsDirectory:/}")
	private String DOCS_DIRECTORY;

	public static final String META_DATA_FILE_NAME = "metadata.properties";

	private static final String THUMBNAIL = "thumbnail.png";
	private static final int THUMBNAIL_WIDTH = 96;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@PostConstruct
	public void init() {
		createDirectory(DOCS_DIRECTORY);
	}

	/**
	 * Inserts a document to the archive by creating a folder with the UUID of the document. In the folder the document is saved and a properties file with the meta data of the document.
	 *
	 */
	@Override
	public void save(final AitDocumentMetadataVO metadata) {
		try {
			final File path = createDirectory(metadata);
			saveFileData(metadata, path);
			saveMetaData(metadata, path);
		} catch (final IOException e) {
			final String message = "Error mientras se guardaba el documento";
			AitLogger.error(logger, message, e);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public String getFilePath(final String subFolder, final String uuid) {
		try {
			String documentPath = getDirectoryPath(subFolder, uuid);
			final File documentDir = new File(documentPath);
			if (!documentDir.exists()) {
				return null;
			}
			final AitDocumentMetadataVO metadata = loadMetadata(documentDir);
			final StringBuilder sb = new StringBuilder();
			sb.append(documentPath).append(IAitConstants.SEPARATOR).append(metadata.getFileName());
			return sb.toString();
		} catch (final IOException e) {
			final String message = "Error mientras se consultaba el documento con id: " + uuid;
			AitLogger.error(logger, message, e);
			throw new RuntimeException(message, e);
		}
	}

	@Override
	public AitDocumentMetadataVO load(final String subFolder, final String uuid) {
		return this.load(subFolder, uuid, false);
	}

	@Override
	public AitDocumentMetadataVO load(final String subFolder, final String uuid, final boolean thumbnail) {
		try {
			return loadDocument(subFolder, uuid, thumbnail);
		} catch (final IOException e) {
			final String message = "Error mientras se cargaba el documento con id: " + uuid;
			AitLogger.error(logger, message, e);
			throw new RuntimeException(message, e);
		}
	}

	private AitDocumentMetadataVO loadDocument(final String subFolder, final String uuid, final boolean thumbnail) throws IOException {
		String documentPath = getDirectoryPath(subFolder, uuid);
		final File documentDir = new File(documentPath);
		if (!documentDir.exists()) {
			return null;
		}
		final AitDocumentMetadataVO metadata = loadMetadata(documentDir);

		boolean update = !uuid.equals(metadata.getUuid()) || !subFolder.equals(metadata.getSubfolder());
		if (update) {
			metadata.setSubfolder(subFolder);
			metadata.setUuid(uuid);
		}
		try {
			final Path path = Paths.get(getFilePath(documentPath, metadata.getFileName(), thumbnail));
			metadata.setFileData(Files.readAllBytes(path));

			if (update) {
				saveMetaData(metadata, documentDir);
			}
		} catch (

		final Exception ex) {
			AitLogger.error(logger, "No se ha podido cargar el archivo, sin embargo se continúa la ejecución", ex);
		}
		return metadata;
	}

	private AitDocumentMetadataVO loadMetadata(final File documentDir) throws IOException {
		AitDocumentMetadataVO metadata = null;
		if (documentDir.exists()) {
			final Properties properties = readProperties(documentDir);
			metadata = new AitDocumentMetadataVO(properties);
		}
		return metadata;
	}

	private String getFilePath(final String documentPath, final String fileName, final boolean thumbnail) {
		final StringBuilder sb = new StringBuilder();
		sb.append(documentPath).append(IAitConstants.SEPARATOR).append(thumbnail ? THUMBNAIL : fileName);
		return sb.toString();
	}

	private void saveFileData(final AitDocumentMetadataVO metadata, final File path) throws IOException {
		final File file = new File(path, metadata.getFileName());
		final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
		stream.write(metadata.getFileData());
		stream.close();
		if (canCreateThumbnail(file.getName()) && !saveThumbnail(file, path)) {
			// si no se pudo crear la imagen en miniatura, se guarda la actual como tal
			final BufferedOutputStream thumbnail = new BufferedOutputStream(new FileOutputStream(new File(path, THUMBNAIL)));
			thumbnail.write(metadata.getFileData());
			thumbnail.close();
		}
	}

	private boolean canCreateThumbnail(String fileName) {
		String[] suffixes = ImageIO.getReaderFileSuffixes();
		for (String suffix : suffixes) {
			if (fileName.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	private boolean saveThumbnail(final File file, final File path) throws IOException {
		BufferedImage originalBufferedImage = null;
		try {

			originalBufferedImage = ImageIO.read(file);

			int widthToScale, heightToScale;
			if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {

				heightToScale = (int) (1.1 * THUMBNAIL_WIDTH);
				widthToScale = (int) (heightToScale * 1.0 / originalBufferedImage.getHeight() * originalBufferedImage.getWidth());

			} else {
				widthToScale = (int) (1.1 * THUMBNAIL_WIDTH);
				heightToScale = (int) (widthToScale * 1.0 / originalBufferedImage.getWidth() * originalBufferedImage.getHeight());
			}
			final BufferedImage resizedImage = new BufferedImage(widthToScale, heightToScale, originalBufferedImage.getType());
			final Graphics2D g = resizedImage.createGraphics();

			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			g.drawImage(originalBufferedImage, 0, 0, widthToScale, heightToScale, null);
			g.dispose();

			final int x = (resizedImage.getWidth() - THUMBNAIL_WIDTH) / 2;
			final int y = (resizedImage.getHeight() - THUMBNAIL_WIDTH) / 2;

			if (x > 0 && y > 0) {
				ImageIO.write(resizedImage, "PNG", new File(path, THUMBNAIL));
				return true;
			}
			return false;
		} catch (final IOException ioe) {
			System.out.println("Error creando el thumbnail: " + path.getPath());
			throw ioe;
		}

	}

	public void saveMetaData(final AitDocumentMetadataVO metadata, final File path) throws IOException {
		final Properties props = metadata.createProperties();
		final File f = new File(path, META_DATA_FILE_NAME);
		final OutputStream out = new FileOutputStream(f);
		props.store(out, "Document meta data");
		out.close();
	}

	private Properties readProperties(final File documentDir) throws IOException {
		final Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(new File(documentDir, META_DATA_FILE_NAME));
			prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	private String getDirectoryPath(final AitDocumentMetadataVO metadata) {
		String group = sdf.format(new Date());
		metadata.setUuid(group + metadata.getUuid());
		return getDirectoryPath(metadata.getSubfolder(), metadata.getUuid());
	}

	private String getDirectoryPath(String subFolder, String uuid) {
		final StringBuilder sb = new StringBuilder();		
		sb.append(this.DOCS_DIRECTORY).append(uuid.substring(0,8)).append(IAitConstants.SEPARATOR).append(subFolder).append(IAitConstants.SEPARATOR).append(uuid.substring(8));
		return sb.toString();
	}

	private File createDirectory(final AitDocumentMetadataVO metadata) {
		final String path = getDirectoryPath(metadata);
		return createDirectory(path);
	}

	private File createDirectory(final String path) {
		final File file = new File(path);
		file.mkdirs();
		return file;
	}

}