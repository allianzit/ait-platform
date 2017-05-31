/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ait.platform.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author AllianzIT
 *
 */
@Component
@Data
public class AitConfigConstants {

	public static String profile;

	public static boolean development;

	@Value("${spring.profiles.active}")
	public void init(String _profile) {
		String[] list = _profile.split(",");
		profile = list[list.length - 1];
		development = _profile.indexOf(",dev,") > -1;
	}

}
