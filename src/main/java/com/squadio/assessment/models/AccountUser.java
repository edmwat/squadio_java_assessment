package com.squadio.assessment.models;

public class AccountUser {
	private String id;
	private String name;

	public AccountUser() {}
	public AccountUser(String id, String name) {
		this.id = id;
		this.name =name;
	}
	
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
}
