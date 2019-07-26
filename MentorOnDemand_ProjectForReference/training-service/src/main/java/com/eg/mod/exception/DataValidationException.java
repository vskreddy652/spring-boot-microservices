package com.eg.mod.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Exception ex;

	public DataValidationException(String message) {
		super(message);
	}

	public DataValidationException(Exception e, String message) {
		super(message);
		ex = e;
	}

	public Exception getEx() {
		return ex;
	}
}
