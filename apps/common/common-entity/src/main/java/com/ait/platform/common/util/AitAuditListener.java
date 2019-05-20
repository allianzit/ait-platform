package com.ait.platform.common.util;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.ait.platform.common.model.entity.AitAudit;
import com.ait.platform.common.model.entity.AitAuditable;

public class AitAuditListener {

	@PrePersist
	public void setCreatedOn(AitAuditable auditable) {
		AitAudit audit = auditable.getAudit();
		if (audit == null) {
			audit = new AitAudit();
			auditable.setAudit(audit);
		}
		audit.setCreatedOn(new Date());
		audit.setCreatedBy(AitUtils.getUserName());
	}

	@PreUpdate
	public void setUpdatedOn(AitAuditable auditable) {
		AitAudit audit = auditable.getAudit();
		if (audit == null) {
			setCreatedOn(auditable);
			audit = auditable.getAudit();
		}
		audit.setUpdatedOn(new Date());
		audit.setUpdatedBy(AitUtils.getUserName());
	}

}