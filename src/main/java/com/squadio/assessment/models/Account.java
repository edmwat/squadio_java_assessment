package com.squadio.assessment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
	private String id;
	private String accountType;
	private String accountNumber;
	@JsonProperty
	private String IBAN;
	private Double balance;
	private String currency;
	
	public Account() {}
	public Account(String id, String accountType, String accountNumber, String iBAN, Double balance, String currency) {
		this.id = id;
		this.accountType = accountType;
		this.accountNumber = accountNumber;
		this.IBAN = iBAN;
		this.balance = balance;
		this.currency = currency;
	}


	public String getId() {
		return id;
	}
	public String getAccountType(){
		return accountType;
	}
	public String getAccountNumber(){ 
		return accountNumber;
	}
	public String getIBAN(){
		return IBAN;
	}
	public Double getBalance(){
		return balance;
	}
	public String getCurrency(){
		return currency;
	}
}
