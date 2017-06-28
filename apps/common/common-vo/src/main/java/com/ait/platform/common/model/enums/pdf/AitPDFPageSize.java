package com.ait.platform.common.model.enums.pdf;

public enum AitPDFPageSize {
	A4("A4"), LETTER("Letter"), FOLIO("Folio"), LEGAL("Legal");

	String descr;

	private AitPDFPageSize(String descr) {
		this.descr = descr;
	}

	public String getDescr() {
		return descr;
	}
}