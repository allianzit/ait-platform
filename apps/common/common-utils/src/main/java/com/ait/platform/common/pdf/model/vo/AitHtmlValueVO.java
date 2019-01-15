package com.ait.platform.common.pdf.model.vo;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AitHtmlValueVO {
	private String key = null;
	private String value = null;
	private boolean escapeHTML = true;
	private List<AitHtmlValueVO> values = new ArrayList<>();

	public void addValue(String key, String value) {
		AitHtmlValueVO vo = new AitHtmlValueVO();
		vo.key = key;
		vo.value = value;
		vo.escapeHTML = true;
		values.add(vo);
	}

	public AitHtmlValueVO newList(String tag) {
		AitHtmlValueVO vo = new AitHtmlValueVO();
		vo.key = tag;
		values.add(vo);
		return vo;
	}

	public AitHtmlValueVO newListItem() {
		return this.newList(null);
	}

	public boolean containsKey(String property) {
		if (values != null) {
			for (AitHtmlValueVO vo : values) {
				if (property.equals(vo.getKey())) {
					return true;
				}
			}
		}
		return property.equals(key);
	}
}
