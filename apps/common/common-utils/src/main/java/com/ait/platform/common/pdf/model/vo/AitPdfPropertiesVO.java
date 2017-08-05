package com.ait.platform.common.pdf.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.ait.platform.common.model.enums.pdf.AitPDFOrientation;
import com.ait.platform.common.model.enums.pdf.AitPDFPageSize;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

import lombok.Data;

@Data
public class AitPdfPropertiesVO {
	private boolean includePageNumber = true;
	private AitPDFOrientation orientation = AitPDFOrientation.PORTRAIT;
	private AitPDFPageSize pageSize = AitPDFPageSize.LETTER;
	private List<AitPdfPageVO> pages = new ArrayList<>();
	private List<Param> additionalParams = new ArrayList<>();

}
