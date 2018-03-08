package com.ait.platform.common.pdf.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.ait.platform.common.model.enums.pdf.AitPDFOrientation;
import com.ait.platform.common.model.enums.pdf.AitPDFPageSize;
import com.github.jhonnymertz.wkhtmltopdf.wrapper.params.Param;

import lombok.Data;

@Data
public class AitPdfPropertiesVO {
	//incluir numero de pagina en cada hoja
	private boolean includePageNumber = true;
	
	//tipo y orientacion por defecto 
	private AitPDFPageSize pageSize = AitPDFPageSize.LETTER;
	private AitPDFOrientation orientation = AitPDFOrientation.PORTRAIT;

	//parametros adicionales de configuracion del PDF
	private List<Param> additionalParams = new ArrayList<>();

	//Informacion de cada pagina del PDF, incluyendo cabecera y pie
	private List<AitPdfPageVO> pages = new ArrayList<>();
	

}
