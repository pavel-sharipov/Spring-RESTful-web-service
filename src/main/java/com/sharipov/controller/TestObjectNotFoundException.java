package com.sharipov.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TestObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6523978385778845060L;

}
