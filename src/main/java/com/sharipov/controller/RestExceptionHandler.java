package com.sharipov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { TestObjectNotFoundException.class })
	protected ResponseEntity<Object> handleConflict(TestObjectNotFoundException ex, WebRequest request) {
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = { TestObjectBadRequestException.class })
	protected ResponseEntity<Object> handleConflict(TestObjectBadRequestException ex, WebRequest request) {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
