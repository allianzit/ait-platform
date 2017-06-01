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
package com.ait.platform.discovery.conf;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang.StringUtils.stripStart;

import java.net.URI;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.util.UriComponentsBuilder;

import de.codecentric.boot.admin.discovery.EurekaServiceInstanceConverter;
import de.codecentric.boot.admin.discovery.ServiceInstanceConverter;
import de.codecentric.boot.admin.model.Application;

/**
 * Converts any {@link ServiceInstance}s to {@link Application}s. To customize
 * the health- or management-url for all applications you can set
 * healthEndpointPath or managementContextPath respectively. If you want to
 * influence the url per service you can add
 * <code>management.context-path</code> or <code>health.path</code> to the
 * instances metadata.
 *
 * @author Johannes Edmeier
 */
public class AitEurekaClientConverter extends EurekaServiceInstanceConverter implements ServiceInstanceConverter {
	private static final String KEY_MANAGEMENT_PATH = "management.context-path";
	private String managementContextPath = "";

	@Override
	protected URI getManagementUrl(ServiceInstance instance) {
		URI health = getHealthUrl(instance);

		String managamentPath = defaultIfEmpty(instance.getMetadata().get(KEY_MANAGEMENT_PATH), managementContextPath);
		managamentPath = stripStart(managamentPath, "/");
		String host = health.getScheme() + "://" + health.getHost() + ":" + health.getPort();

		return UriComponentsBuilder.fromUri(URI.create(host)).pathSegment(managamentPath).build().toUri();
	}

}
