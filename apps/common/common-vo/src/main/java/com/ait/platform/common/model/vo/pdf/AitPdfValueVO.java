package com.ait.platform.common.model.vo.pdf;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class AitPdfValueVO {
	private String key = null;
	private String value = null;
	private List<AitPdfValueVO> values = new ArrayList<>();

	public void addValue(String key, String value) {
		AitPdfValueVO vo = new AitPdfValueVO();
		vo.key = key;
		vo.value = value;
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
