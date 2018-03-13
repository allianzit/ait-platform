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

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ait.platform.common.pdf.model.vo.AitHtmlValueVO;

/**
 * @author AllianzIT
 *
 */
public class AitStringUtils {

	private static final Logger logger = LoggerFactory.getLogger(AitStringUtils.class);

	// indica una propiedad Ej: ::name:: -> obj.getName()
	public static final String PROPERY_HOLDER = "::";

	// indica el separador de metodos para una propiedad. Ej user__name -> obj.getUser().getName()
	private static final String PROPERTY_SEPARATOR = "__";

	// separador que contiene el formato y el valor por defecto para una propiedad. Ej: fecha#yyyy-MM-dd#2018-01-01
	private static final String FORMAT_SEPARATOR = "#";

	// indica un metodo al cual no se le debe agregar el 'get' inicial. Ej dato__valor_floatValue -> obj.getDato().floatValue()
	private static final String NO_GET_METHOD = "_";

	// determina el comienzo de una lista. Ej: ::<roles:: -> las propiedades dentro del tag (hasta encontrar el de cierre) se toman como hijas de la propiedad actual
	public static final String LIST_INI = "<";

	// determina el final de una lista. Ej: ::</roles::
	public static final String LIST_END = "</";

	private AitStringUtils() {
	}

	public static String removeSpaces(final String str) {
		return str.replaceAll("\\s", "").replaceAll("\\.", "").replaceAll("\\,", "");
	}

	public static boolean isEmpty(final Object str) {
		return str == null || str.toString().trim().isEmpty();
	}

	private static Object getValue(Object obj, String method) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String[] data = method.split(FORMAT_SEPARATOR);
		if (obj == null) {
			return data.length > 2 ? data[2] : "";
		}
		int idx = method.indexOf(PROPERTY_SEPARATOR);
		if (idx > -1) {
			String var = method.substring(0, idx);
			Object val = obj.getClass().getMethod("get" + var.substring(0, 1).toUpperCase() + var.substring(1)).invoke(obj);
			return getValue(val, method.substring(idx + 2));
		}
		Object value = null;
		String methodName = "";
		if (data[0].startsWith(NO_GET_METHOD)) {
			if (data[0].contains("(")) {
				data[0] = data[0].replaceAll(")", "");
				data[0] = data[0].replaceAll("(", "_");
				String[] sData = data[0].split("_");
				methodName = sData[0];
				// de momento solo invoca metodos que reciben un solo string Ej: getPropert("nombre");
				value = obj.getClass().getMethod(methodName).invoke(obj, sData[1]/* .split(",") */);
			} else {
				methodName = data[0].substring(1);
				value = obj.getClass().getMethod(methodName).invoke(obj);
			}
		} else {
			methodName = "get" + data[0].substring(0, 1).toUpperCase() + data[0].substring(1);
			value = obj.getClass().getMethod(methodName).invoke(obj);
		}
		if (data.length > 1) {
			// si el valor es nulo, se retorna el valor por defecto (si se definió) o una cadena vacia según sea el caso
			if (value == null) {
				return data.length > 2 ? data[2] : "";
			}
			if (value instanceof Date) {
				SimpleDateFormat sdf = new SimpleDateFormat(data[1]);
				return sdf.format((Date) value);
			}
			if (value instanceof Number) {
				return String.format(data[1], value);
			} else {
				return String.format(data[1], value);
			}
		}
		return value == null ? "" : value;
	}

	public static AitHtmlValueVO getValuesFromTemplate(final AitHtmlValueVO values, String template, Object obj) {
		// cada parametro tiene el siguiente formato: ::campo__campoHijo__campoNieto___getProperty(nombre)___floatValue::
		// (si se ponen 3 signos pesos $$$ quiere decir que el metodo a invocar NO se le debe anteponer la palabra 'get' ni convertir la primer letra a mayusculas)
		// para datos tipo java.util.Date que requieran formato: ::campo.campoHijo#yyyy-MM-dd:: (lo ubicado despues del # corresponde al formato deseado)
		Pattern pattern = Pattern.compile(PROPERY_HOLDER + "(.*?)" + PROPERY_HOLDER);

		Matcher matcher = pattern.matcher(template);
		int idx = 0;
		while (matcher.find(idx)) {
			String property = matcher.group(1);
			try {

				idx = matcher.end();

				// si es un tag de tipo lista (inicio)
				if (property.startsWith(LIST_INI)) {
					// se mueve el indice para omitir las propiedades dentro de la lista
					int newIdx = template.indexOf(LIST_END + property, idx);

					// se busca el tag de cierre de la lista
					AitHtmlValueVO rows = values.newList(property);

					// lista
					Object list = getValue(obj, property.substring(LIST_INI.length() - 1));

					String subTemplate = template.substring(idx, newIdx);

					if (list instanceof Collection<?>) {
						for (Object item : (Collection<?>) list) {
							AitHtmlValueVO row = rows.newListItem();
							getValuesFromTemplate(row, subTemplate, item);
						}
					}
					idx = newIdx;
				} else {// de lo contrario, se asume que es de tipo propiedad
					values.addValue(property, getValue(obj, property).toString());
				}
			} catch (Exception e) {
				logger.error("Error obteniendo valor para la propiedad {0}, Excepcion: {1} ", property, e.getMessage());
				e.printStackTrace();
			}
		}
		return values;
	}

	public static String replaceContent(String content, AitHtmlValueVO values) {
		// valor simple
		if (values.getValue() != null) {
			String newValue = values.isEscapeHTML() ? StringEscapeUtils.escapeHtml4(values.getValue()) : values.getValue();
			String key = PROPERY_HOLDER + values.getKey() + PROPERY_HOLDER;
			content = content.replaceAll(key, Matcher.quoteReplacement(newValue));
		}
		// lista de valores
		else if (values.getKey() != null) {
			// formato: ::<propiedadTipoLista::
			String tagEnd = PROPERY_HOLDER + LIST_END + values.getKey();

			// se copia el segmento entre el tag de inicio y de fin. Formato ::</propiedadTipoLista::
			int begin = content.indexOf(PROPERY_HOLDER + LIST_INI + values.getKey());
			int end = content.indexOf(tagEnd);

			if (begin > -1 && end > -1) {
				// contenido a duplicar por cada registro
				String listString = content.substring(begin, end + tagEnd.length());

				// se quitan los tags que definen cada registro (esos tags no estarán en el HTML final)
				String subContent = listString.substring(tagEnd.length() - 1, listString.length() - tagEnd.length() - 1);

				StringBuffer listBuffer = new StringBuffer();
				// se reemplaza el contenido del tag por lo valores de cada registro de la lista
				for (AitHtmlValueVO value : values.getValues()) {
					listBuffer.append(replaceContent(subContent, value));
				}
				// se reemplaza el contenido en el html principal
				content = content.replace(listString, listBuffer.toString());
			}
		}
		// registro
		else {
			for (AitHtmlValueVO value : values.getValues()) {
				content = replaceContent(content, value);
			}
		}
		return content;
	}

}
