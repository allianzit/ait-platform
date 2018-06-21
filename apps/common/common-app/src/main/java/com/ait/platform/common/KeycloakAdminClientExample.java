package com.ait.platform.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import com.ait.platform.common.model.vo.AitUserVO;

import net.sf.jsefa.Deserializer;
import net.sf.jsefa.csv.CsvIOFactory;

public class KeycloakAdminClientExample {

	public static void main(String[] args) throws Exception {
		String SQL_USER_ATRIBUTE = "Insert into AIT_USER_ATTIBUTE (ID,ATT_KEY,ATT_VALUE,USER_ID) values (%d,'sub','%s',%d);";
		Keycloak kc = KeycloakBuilder.builder() //
//				.serverUrl("https://localhost:8443/auth") //
				.serverUrl("https://rte.vuce.gov.co/auth") //
				.realm("master")//
				.username("admin") //
				.password("Mimojal263!.") //
				.clientId("admin-cli") //
				.resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()) //
				.build();

		UsersResource userResource = kc.realm("ait-platform").users();
		BufferedReader reader = new BufferedReader(new FileReader("C:/Users/msi-gs60/Documents/mincit/RTE/USUARIOS-KEYCLOAK.csv"));

		Deserializer deserializer = CsvIOFactory.createFactory(AitUserVO.class).createDeserializer();
		deserializer.open(reader);
		int idx = 1;
		ArrayList<String> scripts = new ArrayList<>();
		while (deserializer.hasNext()) {
			idx++;
			AitUserVO entity = deserializer.next();
			try {
				boolean verDig = entity.getUsername().indexOf('-') > -1;
				String username = entity.getUsername();

				// si existe un usuario anterior, se borra
				if (verDig) {
					username = entity.getUsername().substring(0, entity.getUsername().indexOf('-'));
					deleteUser(userResource, username);
				}
				deleteUser(userResource, entity.getUsername());

				String rol = "ENSAMBLADORA".equals(entity.getRolesString()) ? "" : "RTE_REQUEST";
				// se define el usuario
				UserRepresentation user = new UserRepresentation();
				user.setEnabled(true);
				user.setUsername(username);
				user.setFirstName(entity.getFirstName().toUpperCase());
				user.setLastName("");
				user.setEmailVerified(true);
				user.setEmail(entity.getEmail() != null ? entity.getEmail().toLowerCase() : "");
				user.setRealmRoles(Arrays.asList(rol));
				user.setAttributes(Collections.singletonMap("rut", Arrays.asList(entity.getUsername())));

				// se crea
				Response result = userResource.create(user);
				if (result.getStatus() == 201) {

					// password temporal
					CredentialRepresentation passwordCred = new CredentialRepresentation();
					passwordCred.setTemporary(true);
					passwordCred.setType(CredentialRepresentation.PASSWORD);
					passwordCred.setValue(username);

					String locationHeader = result.getHeaderString("Location");
					String userId = locationHeader.replaceAll(".*/(.*)$", "$1");

					// Se le asigna el password temporal
					userResource.get(userId).resetPassword(passwordCred);

					// script de insert en ait_user
					scripts.add(String.format(SQL_USER_ATRIBUTE, idx, userId, entity.getId()));
				} else {
					scripts.add("No se pudo crear el usuario." + entity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		scripts.forEach(x -> System.out.println(x));
		deserializer.close(true);
		reader.close();

	}

	private static void deleteUser(UsersResource userResource, String username) {
		List<UserRepresentation> users = userResource.search(username);
		if (!users.isEmpty()) {
			userResource.delete(users.get(0).getId());
		}
	}

}