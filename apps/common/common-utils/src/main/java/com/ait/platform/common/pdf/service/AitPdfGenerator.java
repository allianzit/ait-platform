package com.ait.platform.common.pdf.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.enums.pdf.AitPDFOrientation;
import com.ait.platform.common.model.enums.pdf.AitPDFPageSize;
import com.ait.platform.common.model.enums.pdf.EAitPdfOrigin;
import com.ait.platform.common.pdf.model.vo.AitPdfPageVO;
import com.ait.platform.common.pdf.model.vo.AitPdfPropertiesVO;
import com.ait.platform.common.pdf.model.vo.AitPdfValueVO;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.Pdf;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

public class AitPdfGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(AitPdfGenerator.class);

	private static final String QUIET_PARAM = "-q";
	private static final String ORIENTATION_PARAM = "--orientation";
	private static final String PAGE_SIZE_PARAM = "--page-size";
	private static final String PAGE_NUMBER_PARAM = "--footer-right";
	private static final String PAGE_NUMBER_VALUE = "[page]/[topage]";
	private static final String HEADER_PARAM = "--header-html";
	private static final String FOOTER_PARAM = "--footer-html";

	public static byte[] createPdf(AitPdfPropertiesVO properties) {
		Pdf pdf = new Pdf();
		try {
			for (AitPdfPageVO page : properties.getPages()) {
				String content = page.getContent();
				// se reemplazan los parametros
				boolean fromURL = EAitPdfOrigin.URL.equals(page.getOrigin());
				if (fromURL) {
					LOGGER.debug("Page content from url: {}. We can't replace content", page.getContent());
				} else {
					content = replaceContent(page.getOrigin(), content, page.getValues());

				}
				if (page.isHeader()) {
					addPageParam(pdf, HEADER_PARAM, fromURL, content);
				} else if (page.isFooter()) {
					addPageParam(pdf, FOOTER_PARAM, fromURL, content);
				} else {
					if (fromURL) {
						pdf.addPageFromUrl(content);
					} else {
						pdf.addPageFromString(content);
					}
				}
			}
			pdf.addParam(new Param(QUIET_PARAM));
			pdf.addParam(new Param(ORIENTATION_PARAM, properties.getOrientation().getDescr()));
			pdf.addParam(new Param(PAGE_SIZE_PARAM, properties.getPageSize().getDescr()));

			for (Param param : properties.getAdditionalParams()) {
				pdf.addParam(param);
			}
			if (properties.isIncludePageNumber()) {
				pdf.addParam(new Param(PAGE_NUMBER_PARAM, PAGE_NUMBER_VALUE));
			}

			// se retorna el PDF en forma binaria
			return pdf.getPDF();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String replaceContent(EAitPdfOrigin origin, String content, AitPdfValueVO values) {
		// read file into stream, try-with-resources
		if (EAitPdfOrigin.FILE.equals(origin)) {
			StringBuilder contentBuilder = new StringBuilder();
			try (Stream<String> stream = Files.lines(Paths.get(content))) {
				stream.forEach(contentBuilder::append);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return replaceContent(contentBuilder.toString(), values);
		}
		return replaceContent(content, values);
	}

	private static String replaceContent(String content, AitPdfValueVO values) {
		// valor simple
		if (values.getValue() != null) {
			String newValue = values.isEscapeHTML() ? StringEscapeUtils.escapeHtml4(values.getValue()) : values.getValue();
			content = content.replaceAll("::" + values.getKey() + "::", Matcher.quoteReplacement(newValue));
		}
		// lista de valores
		else if (values.getKey() != null) {
			String tagEnd = "</" + values.getKey() + ">";
			int begin = content.indexOf("<" + values.getKey() + ">");
			int end = content.indexOf(tagEnd);
			if (begin > -1 && end > -1) {
				String listString = content.substring(begin, end + tagEnd.length());
				String subContent = listString.substring(tagEnd.length() - 1, listString.length() - tagEnd.length() - 1);
				StringBuffer listBuffer = new StringBuffer();
				for (AitPdfValueVO value : values.getValues()) {
					listBuffer.append(replaceContent(subContent, value));
				}
				content = content.replace(listString, listBuffer.toString());
			}
		}
		// registro
		else {
			for (AitPdfValueVO value : values.getValues()) {
				content = replaceContent(content, value);
			}
		}
		return content;
	}

	private static void addPageParam(Pdf pdf, String paramName, boolean fromURL, String content) throws IOException {
		String uri = content;
		// si no es una referencia a url
		if (!fromURL) {
			uri = createFileAndGetPath(content);
		}
		pdf.addParam(new Param(paramName, uri));
	}

	private static String createFileAndGetPath(String content) throws IOException {
		File temp = File.createTempFile("ait-pdf-temp-" + UUID.randomUUID().toString(), ".html");
		FileUtils.writeStringToFile(temp, content, "UTF-8");
		return temp.getAbsolutePath();
	}

	public static int k = 0;

	public static void main(String[] args) {
		for (k = 0; k < 10; k++) {
			createPdfTest(k);
		}

	}

	private static void createPdfTest(int id) {
		System.out.println(id + ":" + (System.currentTimeMillis()));
		long ini = System.currentTimeMillis();
		new Thread(new Runnable() {

			@Override
			public void run() {
				final String content = "C:/desarrollo/wsMinCIT/rte-app/templates/letter-approve.html";
				final String header = "C:/desarrollo/wsMinCIT/rte-app/templates/header.html";
				final String footer = "C:/desarrollo/wsMinCIT/rte-app/templates/footer-letter.html";

				// for (int i = 0; i < 1; i++) {
				String name = "d:\\prueba" + id + ".pdf";
				List<AitPdfPageVO> pages = new ArrayList<>();

				AitPdfValueVO values = new AitPdfValueVO();
				AitPdfPageVO headerPage = new AitPdfPageVO(EAitPdfOrigin.FILE, header, values);
				headerPage.setHeader(true);
				AitPdfPageVO footerPage = new AitPdfPageVO(EAitPdfOrigin.FILE, footer, values);
				footerPage.setFooter(true);
				pages.add(headerPage);

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
				// pages.add(new AitPdfPageVO(EAitPdfOrigin.STRING, "<!DOCTYPE HTML><html><head></head><body><div align='center' style='font-weight: bold'>HOLA!!!</div></body></html>"));
				pages.add(footerPage);

				AitPdfPropertiesVO properties = new AitPdfPropertiesVO();
				properties.setPages(pages);
				properties.setIncludePageNumber(false);
				properties.setOrientation(AitPDFOrientation.PORTRAIT);
				properties.setPageSize(AitPDFPageSize.LETTER);
				properties.getAdditionalParams().add(new Param("-L", "20mm"));
				properties.getAdditionalParams().add(new Param("-R", "20mm"));
				properties.getAdditionalParams().add(new Param("-B", "40mm"));
				byte[] data = AitPdfGenerator.createPdf(properties);

				BufferedOutputStream stream;
				try {
					final File file = new File(name);
					stream = new BufferedOutputStream(new FileOutputStream(file));
					stream.write(data);
					stream.close();
				} catch (IOException e) {
					throw new AitException(HttpStatus.BAD_REQUEST, "Error creando el adjunto", Arrays.asList(new String[0]), e);
				}

				System.out.println(name + " - " + (System.currentTimeMillis() - ini));
				// }
			}
		}).start();

	}
}