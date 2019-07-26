package com.eg.mod.exception;

import java.util.Date;

public class ErrorDetails {

	private Date timestamp;
	private int errorCode;
	private String message;
	private String details;

	public ErrorDetails(Date timestamp, int errorCode, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.errorCode = errorCode;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
