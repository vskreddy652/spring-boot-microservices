package com.eg.mod.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {

	private int status;
	private String message;
	private Object result;

	public ApiResponse(@JsonProperty("status") int status, @JsonProperty("message") String message,
			@JsonProperty("result") Object result) {
		this.status = status;
		this.message = message;
		this.result = result;
	}

	public ApiResponse(@JsonProperty("status") int status, @JsonProperty("message") String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}