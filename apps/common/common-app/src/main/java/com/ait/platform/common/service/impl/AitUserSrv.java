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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ait.platform.common.model.entity.AitUserAttribute;
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

	private static List<String> attributes = Arrays.asList("given_name", "family_name", "email", "name", "preferred_username", "authorities");

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

		String username = getUsername();

		AitUser user = userRepo.getByUsername(username);
		// se crea el usuario si no existe
		if (user == null) {
			logger.info("Creating new user {}", username);
			user = new AitUser();
			user.setUsername(username);
			user.setFirstName(details.get("given_name"));
			user.setLastName(details.get("family_name"));
			user.setAttributes(new HashSet<>());
			user.setEnabled(true);
		}
		AitUserVO userVO = new AitUserVO();
		try {
			user.setEmail(details.get("email"));
			BeanUtils.copyProperties(user, userVO);

			// se leen los atributos adicionales del usuario provenientes de keycloak
			for (String key : details.keySet()) {
				// si es un atributo diferente a los basicos
				if (!attributes.contains(key)) {
					AitUserAttribute att = getAttribute(user, key);
					att.setValue(details.get(key));
					userVO.getAttributes().put(key, att.getValue());
				}
			}

			SecurityContextHolder.getContext().getAuthentication();
			// se crea el menu del usuario
			Collection<GrantedAuthority> authorities = principal.getAuthorities();
			if (!authorities.isEmpty()) {
				StringBuffer sb = new StringBuffer();
				for (GrantedAuthority auth : authorities) {
					userVO.getRoles().add(auth.getAuthority());
					sb.append(",").append(auth.getAuthority()).append(",");
				}
				user.setUserRoles(sb.toString());
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
			userRepo.save(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userVO;
	}

	private AitUserAttribute getAttribute(AitUser user, String key) {
		for (AitUserAttribute att : user.getAttributes()) {
			if (att.getKey().equals(key)) {
				return att;
			}
		}
		// si no existe, se agrega
		AitUserAttribute att = new AitUserAttribute(null, user, key, "");
		user.getAttributes().add(att);
		return att;
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

	@Override
	@Transactional(readOnly = true)
	public AitUserVO getById(Integer userId) {
		return buildVO(userRepo.getOne(userId));
	}

	@Override
	@Transactional(readOnly = true)
	public AitUserVO getByUsername(String username) {
		return buildVO(userRepo.getByUsername(username));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AitUserVO> getByRole(String role) {
		return userRepo.findByRole("%," + role + ",%").stream().map(opt -> convertAToB(opt, new AitUserVO())).collect(Collectors.toList());
	}

	private AitUserVO buildVO(AitUser user) {
		if (user != null) {
			AitUserVO vo = convertAToB(user, new AitUserVO());
			// se leen los atributos adicionales del usuario provenientes de keycloak
			for (AitUserAttribute att : user.getAttributes()) {
				vo.getAttributes().put(att.getKey(), att.getValue());
			}
			return vo;
		}
		return null;
	}
}
