package com.ait.platform.common.pdf.model.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AitPdfValueVO {
	private String key = null;
	private String value = null;
	private boolean escapeHTML = true;
	private List<AitPdfValueVO> values = new ArrayList<>();

	public void addValue(String key, String value) {
		addValue(key, value, true);
	}

	public void addValue(String key, String value, boolean escapeHTML) {
		AitPdfValueVO vo = new AitPdfValueVO();
		vo.key = key;
		vo.value = value;
		vo.escapeHTML = escapeHTML;
		values.add(vo);

	}

	public AitPdfValueVO newList(String tag) {
		AitPdfValueVO vo = new AitPdfValueVO();
		vo.key = tag;
		values.add(vo);
		return vo;
	}

	public AitPdfValueVO newListItem() {
		return this.newList(null);
	}

}
