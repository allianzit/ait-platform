package com.ait.platform.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.enums.pdf.AitPDFOrientation;
import com.ait.platform.common.model.enums.pdf.AitPDFPageSize;
import com.ait.platform.common.model.enums.pdf.EAitPdfOrigin;
import com.ait.platform.common.model.vo.pdf.AitPdfPageVO;
import com.ait.platform.common.model.vo.pdf.AitPdfPropertiesVO;
import com.ait.platform.common.model.vo.pdf.AitPdfValueVO;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

public class AitPdfGenarator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AitPdfGenarator.class);

	private static final String QUIET_PARAM = "-q";
	private static final String ORIENTATION_PARAM = "--orientation";
	private static final String PAGE_SIZE_PARAM = "--page-size";
	private static final String PAGE_NUMBER_PARAM = "--footer-right";
	private static final String PAGE_NUMBER_VALUE = "[page]/[topage]";
	private static final String HEADER_PARAM = "--header-html";
	private static final String FOOTER_PARAM = "--footer-html";

	private static String headerUri = "";
	private static String footerUri = "";

	@PostConstruct
	private void init() throws IOException {
		headerUri = createFileAndGetPath("");
		footerUri = createFileAndGetPath("");
	}

	public static byte[] createPdf(AitPdfPropertiesVO properties) {
		return createPdfInternal(properties, null);
	}

	public static void createPdf(AitPdfPropertiesVO properties, String path) {
		createPdfInternal(properties, path);
	}

	private static byte[] createPdfInternal(AitPdfPropertiesVO properties, String path) {
		Pdf pdf = new Pdf();
		try {
			AitPdfPageVO customHeader = null;
			AitPdfPageVO customFooter = null;
			for (AitPdfPageVO page : properties.getPages()) {
				if (page.isCustomHeader()) {
					customHeader = page;
				} else if (page.isCustomFooter()) {
					customFooter = page;
				} else {
					addContentPage(pdf, page);
				}
			}
			pdf.addParam(new Param(QUIET_PARAM));
			pdf.addParam(new Param(ORIENTATION_PARAM, properties.getOrientation().getDescr()));
			pdf.addParam(new Param(PAGE_SIZE_PARAM, properties.getPageSize().getDescr()));

			if (properties.isIncludePageNumber()) {
				pdf.addParam(new Param(PAGE_NUMBER_PARAM, PAGE_NUMBER_VALUE));
			}
			if (properties.isIncludeHeader()) {
				addPageParam(pdf, HEADER_PARAM, customHeader, headerUri);
			}
			if (properties.isIncludeFooter()) {
				addPageParam(pdf, FOOTER_PARAM, customFooter, footerUri);
			}

			for (Param param : properties.getAdditionalParams()) {
				pdf.addParam(param);
			}

			if (path == null) {
				return pdf.getPDF();
			}
			pdf.saveAs(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static void addContentPage(Pdf pdf, AitPdfPageVO page) {
		// si se deben realizar reemplazos de contenido
		if (page.getValues() != null) {
			if (page.getOrigin().equals(EAitPdfOrigin.URL)) {
				throw new AitException(HttpStatus.BAD_REQUEST, "No se pueden reemplazar valores", "El origen no puede ser de tipo URL");
			}
			// read file into stream, try-with-resources
			StringBuilder contentBuilder = new StringBuilder();
			try (Stream<String> stream = Files.lines(Paths.get(page.getContent()))) {
				stream.forEach(contentBuilder::append);

			} catch (IOException e) {
				e.printStackTrace();
			}
			String newContent = replaceContent(contentBuilder.toString(), page.getValues());
			pdf.addPageFromString(newContent);
		} else {
			switch (page.getOrigin()) {
			case STRING:
				pdf.addPageFromString(page.getContent());
				break;
			case FILE:
				pdf.addPageFromFile(page.getContent());
				break;
			case URL:
				pdf.addPageFromUrl(page.getContent());
				break;
			}
		}

	}

	private static String replaceContent(String content, AitPdfValueVO values) {
		// valor simple
		if (values.getValue() != null) {
			content = content.replaceAll("::" + values.getKey() + "::", values.getValue());
		}
		// lista de valores
		else if (values.getKey() != null) {
			String tagEnd = "</" + values.getKey() + ">";
			String listString = content.substring(content.indexOf("<" + values.getKey() + ">"), content.indexOf(tagEnd) + tagEnd.length());
			String subContent = listString.substring(tagEnd.length() - 1, listString.length() - tagEnd.length() - 1);
			StringBuffer listBuffer = new StringBuffer();
			for (AitPdfValueVO value : values.getValues()) {
				listBuffer.append(replaceContent(subContent, value));
			}
			content = content.replace(listString, listBuffer.toString());
		}
		// registro
		else {
			for (AitPdfValueVO value : values.getValues()) {
				content = replaceContent(content, value);
			}
		}
		return content;
	}

	private static void addPageParam(Pdf pdf, String paramName, AitPdfPageVO customHeader, String defaultValue) throws IOException {
		String uri = defaultValue;
		if (customHeader != null) {
			if (EAitPdfOrigin.STRING.equals(customHeader.getOrigin())) {
				uri = createFileAndGetPath(customHeader.getContent());
			} else {
				uri = customHeader.getContent();
			}
		}
		if (uri.isEmpty()) {
			LOGGER.warn("No se definió apropiadamente la uri del parametro {}", paramName);
		} else {
			pdf.addParam(new Param(paramName, uri));
		}
	}

	private static String createFileAndGetPath(String content) throws IOException {
		File temp = File.createTempFile("ait-pdf-temp-" + UUID.randomUUID().toString(), ".html");
		FileUtils.writeStringToFile(temp, content, "UTF-8");
		return temp.getAbsolutePath();
	}

	public static void main(String[] args) {

		final String content = "C:/desarrollo/wsMinCIT/rte-app/templates/planilla-a.html";
		final String header = "C:/desarrollo/wsMinCIT/rte-app/templates/header.html";
		final String footer = "C:/desarrollo/wsMinCIT/rte-app/templates/footer.html";

		for (int i = 0; i < 1; i++) {
			long ini = System.currentTimeMillis();
			String name = "d:\\prueba" + i + ".pdf";
			List<AitPdfPageVO> pages = new ArrayList<>();
			AitPdfPageVO headerPage = new AitPdfPageVO(EAitPdfOrigin.FILE, header);
			headerPage.setCustomHeader(true);
			AitPdfPageVO footerPage = new AitPdfPageVO(EAitPdfOrigin.FILE, footer);
			footerPage.setCustomFooter(true);
			pages.add(headerPage);

			AitPdfValueVO values = new AitPdfValueVO();
			values.addValue("motopartName", "Nombre de la empresa motopartista!!!");
			values.addValue("motopartNIT", "321654654-8!!!");

			// lista de motopartes
			AitPdfValueVO rows = values.newList("motopartItem");
			for (int j = 0; j < 40; j++) {
				int totalSupplies = new Random().nextInt(19);

				// nuevo item motoparte
				AitPdfValueVO row = rows.newListItem();

				// celdas de la fila
				row.addValue("descMotopart", "descripción " + j);
				row.addValue("partNumber", "3215" + j);
				row.addValue("subpart", "24654" + j);
				row.addValue("rowspan", "" + (totalSupplies + 1));

				// lista de suministros de la motoparte
				AitPdfValueVO supplies = row.newList("supplyItem");
				for (int k = 0; k < totalSupplies; k++) {
					AitPdfValueVO supply = supplies.newListItem();
					supply.addValue("subpartSupply", "32554-" + (k + 1));
					supply.addValue("van", "111" + (k + 1) + " - " + totalSupplies);
				}
			}

			pages.add(new AitPdfPageVO(EAitPdfOrigin.FILE, content, values));
			pages.add(new AitPdfPageVO(EAitPdfOrigin.STRING, "<!DOCTYPE HTML><html><head></head><body><div align='center' style='font-weight: bold'>HOLA!!!</div></body></html>"));
			pages.add(footerPage);

			AitPdfPropertiesVO properties = new AitPdfPropertiesVO();
			properties.setPages(pages);
			properties.setOrientation(AitPDFOrientation.LANDSCAPE);
			properties.setPageSize(AitPDFPageSize.LEGAL);
			AitPdfGenarator.createPdf(properties, name);
			System.out.println(name + " - " + (System.currentTimeMillis() - ini));
		}

	}
}