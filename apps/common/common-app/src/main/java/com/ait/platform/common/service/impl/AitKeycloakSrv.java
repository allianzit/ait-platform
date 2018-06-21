package com.ait.platform.common.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import com.ait.platform.common.model.vo.AitUserVO;
import com.ait.platform.common.service.IAitKeycloakSrv;

@Service
public class AitKeycloakSrv implements IAitKeycloakSrv {

	private static RealmResource realmResource;
	private static UsersResource userResource;

	@PostConstruct
	public void init() {
		Keycloak kc = KeycloakBuilder.builder() //
				.serverUrl("https://localhost:8443/auth") //
//				 .serverUrl("https://rte.vuce.gov.co/auth") //
				.realm("master")//
				.username("admin") //
				.password("Mimojal263!.") //
				.clientId("admin-cli") //
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();

		realmResource = kc.realm("ait-platform");
		userResource = realmResource.users();
	}

	public String createUser(AitUserVO vo) {

		// se valida si existe un usuario con el mismo username
		List<UserRepresentation> users = userResource.search(vo.getUsername());
		if (!users.isEmpty()) {
			throw new RuntimeException("Ya existe un usuario con el mismo username: " + vo.getUsername());
		}

		// se define el usuario
		UserRepresentation user = new UserRepresentation();

		user.setEnabled(true);
		user.setUsername(vo.getUsername());
		user.setFirstName(user.getFirstName().toUpperCase());
		user.setLastName(user.getLastName().toUpperCase());
		user.setEmailVerified(true);
		user.setEmail(user.getEmail() != null ? user.getEmail().toLowerCase() : "");
		user.setRealmRoles(Arrays.asList(vo.getRolesString().split(",")));

		// atributos del usuario
		HashMap<String, String> attrs = vo.getAttributes();
		if (attrs != null && !attrs.isEmpty()) {
			user.setAttributes(new HashMap<String, List<String>>());
			for (String key : attrs.keySet()) {
				user.getAttributes().put(key, Arrays.asList(attrs.get(key)));
			}
		}

		// se crea el usuario
		Response result = userResource.create(user);
		if (result.getStatus() == 201) {

			String locationHeader = result.getHeaderString("Location");
			String userId = locationHeader.replaceAll(".*/(.*)$", "$1");

			// se crea el password temporal
			resetPwd(userId);
			return userId;

		} else {
			throw new RuntimeException("Error creando el usuario: " + vo.getUsername());
		}
	}

	public void updateUser(AitUserVO vo) {

		HashMap<String, String> attrs = vo.getAttributes();

		UserResource resource = userResource.get(attrs.get("sub"));

		// se define el usuario
		UserRepresentation user = resource.toRepresentation();

		user.setEnabled(vo.getEnabled());
		user.setFirstName(user.getFirstName().toUpperCase());
		user.setLastName(user.getLastName().toUpperCase());
		user.setEmailVerified(true);
		user.setEmail(user.getEmail() != null ? user.getEmail().toLowerCase() : "");
		user.setRealmRoles(Arrays.asList(vo.getRolesString().split(",")));

		// atributos del usuario
		user.setAttributes(new HashMap<String, List<String>>());
		for (String key : attrs.keySet()) {
			user.getAttributes().put(key, Arrays.asList(attrs.get(key)));
		}

		// se crea el usuario
		resource.update(user);
	}

	public void deleteUserByUsername(String username) {
		List<UserRepresentation> users = userResource.search(username);
		if (!users.isEmpty()) {
			userResource.delete(users.get(0).getId());
		}
	}

	public void deleteUserById(String userId) {
		userResource.get(userId).remove();
	}

	public void resetPwd(String userId) {
		// password temporal
		UserResource resource = userResource.get(userId);
		UserRepresentation user = resource.toRepresentation();
		if (user.getEmail() != null) {
			String pwd = user.getUsername();

			CredentialRepresentation passwordCred = new CredentialRepresentation();
			passwordCred.setTemporary(true);
			passwordCred.setType(CredentialRepresentation.PASSWORD);
			passwordCred.setValue(pwd);

			// Se le asigna el password temporal
			resource.resetPassword(passwordCred);

			// TODO contemplar el envío de email con el nuevo pwd
			//
			//
		} else {
			throw new RuntimeException("El email del usuario es requerido para reiniciar la clave de acceso");
		}
	}

}
