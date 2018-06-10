package com.example.senate.exception;

public class VotingNotOpenException extends Exception{

	private static final long serialVersionUID = 1L;

    public VotingNotOpenException(String message) {
        super(message);
    }
}
