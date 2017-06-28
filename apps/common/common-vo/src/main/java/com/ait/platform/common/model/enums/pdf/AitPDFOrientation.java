package com.ait.platform.common.model.enums.pdf;

public enum AitPDFOrientation {
	LANDSCAPE("Landscape"), PORTRAIT("Portrait");

	String descr;

	private AitPDFOrientation(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return descr;
	}
}