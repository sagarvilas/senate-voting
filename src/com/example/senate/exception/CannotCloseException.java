package com.example.senate.exception;

public class CannotCloseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    public CannotCloseException( String message) {
        super(message);
    }

}
