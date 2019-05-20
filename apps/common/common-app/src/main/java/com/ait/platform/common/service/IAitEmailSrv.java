package com.ait.platform.common.service;

import java.util.List;

import com.ait.platform.common.model.vo.AitTaskEmailAttachedVO;

public interface IAitEmailSrv extends IAitSrv {

	void sendEmail(String emailSubject, String template, Object data, List<AitTaskEmailAttachedVO> attachedList, String... emails);
}