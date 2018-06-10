package com.example.senate.exception;

public class NumberOfVotesExcededException extends Exception {
	private static final long serialVersionUID = 1L;

	public NumberOfVotesExcededException(String message) {
		super(message);
	}
}
