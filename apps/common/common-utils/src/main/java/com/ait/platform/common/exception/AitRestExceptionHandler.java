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
package com.ait.platform.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author AllianzIT
 *
 */
@RestControllerAdvice
public class AitRestExceptionHandler extends ResponseEntityExceptionHandler {

	// 400

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getRequestPartName() + " part is missing";
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getParameterName() + " parameter is missing";
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	//
	//
	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
		return new ResponseEntity<Object>(AitException, new HttpHeaders(), AitException.getStatus());
	}

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, "Error de integridad referencial", "Verifique que la información relacionada que intenta ingresar sea adecuada");
		return new ResponseEntity<Object>(AitException, new HttpHeaders(), AitException.getStatus());
	}
	
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException ex, final WebRequest request) {
		logger.info(ex.getClass().getName());
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, "Error de integridad de datos", "Verifique que la información que intenta ingresar no se encuentre repetida");
		return new ResponseEntity<Object>(AitException, new HttpHeaders(), AitException.getStatus());
	}

	// 404

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		//
		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final AitException AitException = new AitException(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	// 405

	// @Override
	// protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
	// logger.info(ex.getClass().getName());
	// //
	// final StringBuilder builder = new StringBuilder();
	// builder.append(ex.getMethod());
	// builder.append(" method is not supported for this request. Supported methods are ");
	//
	// for (final MediaType type : ex.getSupportedMediaTypes()) {
	// builder.append(type + " ");
	// }
	//
	// final AitException AitException = new AitException(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
	// return new ResponseEntity<Object>(AitException, new HttpHeaders(), AitException.getStatus());
	// }

	// 415

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		logger.info(ex.getClass().getName());
		ex.printStackTrace();
		//
		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");

		for (final MediaType type : ex.getSupportedMediaTypes()) {
			builder.append(type + " ");
		}

		final AitException AitException = new AitException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	// 500

	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final HttpHeaders headers, final WebRequest request) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final AitException AitException = new AitException(HttpStatus.UNAUTHORIZED, "Acceso no permitido", "Su perfil no cuenta con los permisos necesarios para acceder al servicio solicitado");
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}

	// @ExceptionHandler({ RuntimeException.class })
	// public ResponseEntity<Object> handleAll(final Exception ex, final HttpHeaders headers, final WebRequest request) {
	// logger.info(ex.getClass().getName());
	// logger.error("error", ex);
	// //
	// final AitException AitException = new AitException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error en el procesamiento de la solicitud");
	// return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	// }

	@ExceptionHandler({ RuntimeException.class })
	@ResponseBody
	public ResponseEntity<Object> handleAll(final Exception ex) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		//
		final AitException AitException = new AitException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Error en el procesamiento de la solicitud");
		return new ResponseEntity<Object>(AitException, new HttpHeaders(), AitException.getStatus());
	}

	@ExceptionHandler({ AitException.class })
	public ResponseEntity<Object> handleAitException(final AitException ex) {
		logger.info(ex.getClass().getName());
		logger.error("error", ex);
		return new ResponseEntity<Object>(ex, new HttpHeaders(), ex.getStatus());
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	public ResponseEntity<Object> handleFileException(Exception ex, final HttpHeaders headers, WebRequest request) {
		ex.printStackTrace();
		final AitException AitException = new AitException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), "Error al cargar informacion adicional");
		return handleExceptionInternal(ex, AitException, headers, AitException.getStatus(), request);
	}
}