package com.example.senate.exception;

public class AlreadyVotedException extends Exception {

	private static final long serialVersionUID = 1L;

    public AlreadyVotedException(String message) {
        super(message);
    }

}
