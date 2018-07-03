package com.ait.platform.common.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ait.platform.common.exception.AitException;
import com.ait.platform.common.model.vo.AitUserVO;
import com.ait.platform.common.service.IAitKeycloakSrv;

import lombok.Data;

@Service
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ait.platform.keycloak")
@Data
public class AitKeycloakSrv implements IAitKeycloakSrv {
	private static final String KK_ID = "sub";

	private String serverUrl;
	private String masterRealm;
	private String aitRealm;
	private String username;
	private String password;
	private String clientId;
	private int poolSize;

	private static RealmResource realmResource;
	private static String aitClientId;
	private static UsersResource userResource;

	@PostConstruct
	public void init() {
		Keycloak kc = KeycloakBuilder.builder() //
				.serverUrl(serverUrl) //
				.realm(masterRealm)//
				.username(username) //
				.password(password) //
				.clientId(clientId) //
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(poolSize).build()) //
				.build();

		realmResource = kc.realm(aitRealm);
		aitClientId = realmResource.clients().findByClientId("ait-client").get(0).getId();
		userResource = realmResource.users();
	}

	public String createUser(AitUserVO vo, boolean update) {

		// se valida si existe un usuario con el mismo username
		List<UserRepresentation> users = userResource.search(vo.getUsername());
		if (!users.isEmpty()) {
			if (update) {
				updateUser(vo);
				return null;// si se retorna nulo se asume que el usuario ya existia
			}
			throw new RuntimeException("Ya existe un usuario con el mismo nombre de usuario: " + vo.getUsername());
		}

		// se define el usuario
		UserRepresentation user = new UserRepresentation();

		user.setEnabled(true);
		user.setUsername(vo.getUsername());
		user.setFirstName(vo.getFirstName().toUpperCase());
		user.setLastName(vo.getLastName().toUpperCase());
		user.setEmailVerified(true);
		user.setEmail(vo.getEmail() != null ? vo.getEmail().toLowerCase() : "");

		// se crea el usuario
		Response result = userResource.create(user);

		// atributos del usuario
		HashMap<String, String> attrs = vo.getAttributes();
		if (attrs != null && !attrs.isEmpty()) {
			user.setAttributes(new HashMap<String, List<String>>());
			for (String key : attrs.keySet()) {
				user.getAttributes().put(key, Arrays.asList(attrs.get(key)));
			}
		}

		if (result.getStatus() == 201) {

			String locationHeader = result.getHeaderString("Location");
			String userId = locationHeader.replaceAll(".*/(.*)$", "$1");

			UserResource resource = userResource.get(userId);
			setUserRoles(resource, vo);
			// se crea el password temporal
			resetPwd(userId);
			return userId;

		} else {
			throw new AitException(HttpStatus.BAD_REQUEST, "Error al creat el usuario en keycloak", "No fue posible crear el usuario " + vo.getUsername() + " en keycloak");
		}
	}

	private void setUserRoles(UserResource resource, AitUserVO vo) {
		ArrayList<RoleRepresentation> roles = new ArrayList<RoleRepresentation>();

		ClientResource client = realmResource.clients().get(aitClientId);
		for (String role : vo.getRoles()) {
			roles.add(client.roles().get(role).toRepresentation());
		}
		// borrar los anteriores
		resource.roles().clientLevel(aitClientId).remove(resource.roles().clientLevel(aitClientId).listAll());
		resource.roles().clientLevel(aitClientId).add(roles);

		// se limpia cualquier grupo para evitar asignar roles no deseados
		resource.groups().forEach(g -> {
			resource.leaveGroup(g.getId());
		});
	}

	public void updateUser(AitUserVO vo) {
		HashMap<String, String> attrs = vo.getAttributes();

		String id = attrs.get(KK_ID);
		if (id == null) {
			List<UserRepresentation> users = userResource.search(vo.getUsername());
			if (users.isEmpty()) {
				throw new AitException(HttpStatus.BAD_REQUEST, "Error al actualizar el usuario en keycloak", "No fue posible determinar el identificador del usuario en keycloak");
			}
			id = users.get(0).getId();
		}
		UserResource resource = userResource.get(id);
		// se define el usuario
		UserRepresentation user = resource.toRepresentation();

		user.setEnabled(vo.getEnabled());
		user.setFirstName(vo.getFirstName().toUpperCase());
		user.setLastName(vo.getLastName().toUpperCase());
		user.setEmailVerified(true);
		user.setEmail(vo.getEmail() != null ? vo.getEmail().toLowerCase() : "");

		// atributos del usuario
		user.setAttributes(new HashMap<String, List<String>>());
		for (String key : attrs.keySet()) {
			user.getAttributes().put(key, Arrays.asList(attrs.get(key)));
		}

		setUserRoles(resource, vo);

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
