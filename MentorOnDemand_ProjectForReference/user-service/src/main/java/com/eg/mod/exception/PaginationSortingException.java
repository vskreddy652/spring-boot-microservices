package com.eg.mod.exception;

public class PaginationSortingException extends RuntimeException {

	private static final long serialVersionUID = -123L;
	private Exception ex;

	public PaginationSortingException(String message) {
		super(message);
	}

	public PaginationSortingException(Exception e, String message) {
		super(message);
		ex = e;
	}

	public Exception getEx() {
		return ex;
	}

}
