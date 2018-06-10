package com.example.senate.domain;

public class VicePresident extends Senator {
	public VicePresident(String name) {
		super(name);
	}

	boolean available;

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
