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
package com.ait.platform.common.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ait.platform.common.model.entity.AitMenu;
import com.ait.platform.common.model.entity.AitUser;
import com.ait.platform.common.model.vo.AitMenuVO;
import com.ait.platform.common.model.vo.AitUserVO;
import com.ait.platform.common.repository.IAitMenuRepo;
import com.ait.platform.common.repository.IAitUserRepo;
import com.ait.platform.common.service.IAitUserSrv;

/**
 * @author AllianzIT
 *
 */
@Service
@Transactional
public class AitUserSrv extends AitSrv implements IAitUserSrv {

	private static final Logger logger = LoggerFactory.getLogger(AitUserSrv.class);
	@Autowired
	private IAitUserRepo userRepo;

	@Autowired
	private IAitMenuRepo menuRepo;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public AitUserVO getUserVO() {
		UsernamePasswordAuthenticationToken principal = getAitPrincipal();

		Map<String, String> details = (Map<String, String>) principal.getDetails();

		String username = details.get("preferred_username");

		AitUser user = userRepo.getByUsername(username);
		// se crea el usuario si no existe
		if (user == null) {
			logger.info("Creating new user {}", username);
			user = new AitUser();
			user.setUsername(username);
			user.setFirstName(details.get("given_name"));
			user.setLastName(details.get("family_name"));
			user.setEmail(details.get("email"));
			user.setEnabled(true);
			userRepo.save(user);
		}
		AitUserVO userVO = new AitUserVO();
		try {
			BeanUtils.copyProperties(user, userVO);
			SecurityContextHolder.getContext().getAuthentication();
			// se crea el menu del usuario
			Collection<GrantedAuthority> authorities = principal.getAuthorities();
			if (!authorities.isEmpty()) {
				logger.info("Loading user menu for authorities: {}", authorities);
				List<AitMenu> menuList = menuRepo.findRootOpts();
				for (AitMenu menuOpc : menuList) {
					AitMenuVO opc = new AitMenuVO();
					BeanUtils.copyProperties(menuOpc, opc, "children");
					if (hasAuthority(opc, menuOpc.getChildren(), authorities)) {
						userVO.getMenu().add(opc);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userVO;
	}

	private boolean hasAuthority(AitMenuVO opc, Set<AitMenu> children, Collection<GrantedAuthority> authorities) {

		boolean hasAuthority = false;
		if (children != null && !children.isEmpty()) {
			for (AitMenu currentOpc : children) {
				AitMenuVO menuOpc = new AitMenuVO();
				BeanUtils.copyProperties(currentOpc, menuOpc, "children");
				if (hasAuthority(menuOpc, currentOpc.getChildren(), authorities)) {
					opc.getChildren().add(menuOpc);
					hasAuthority = true;
				}
			}
		} else {
			for (GrantedAuthority auth : authorities) {
				if (opc.getRoles() != null && opc.getRoles().contains(auth.getAuthority())) {
					hasAuthority = true;
					break;
				}
			}
		}
		return hasAuthority;
	}

}
