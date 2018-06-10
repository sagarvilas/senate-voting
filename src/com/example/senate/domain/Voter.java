package com.example.senate.domain;

public interface Voter {

	public void setName(String name);

	public String getName();

	public default boolean isAvailable() {
		return true;
	}

	public default void setAvailable(boolean available) {
		throw new UnsupportedOperationException();
	}
}
