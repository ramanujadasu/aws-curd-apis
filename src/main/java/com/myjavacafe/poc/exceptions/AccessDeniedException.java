package com.myjavacafe.poc.exceptions;

public class AccessDeniedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccessDeniedException() {

        super("Access Denied (Service: Amazon S3; Status Code: 403)");
    }
	public AccessDeniedException(String message) {

        super("Access Denied: "+message);
    }
}
