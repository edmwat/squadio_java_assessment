package com.squadio.assessment.models;

import java.util.Date;

public class Statement {
	private String accountNumber;
	private String description;
	private Double amount;
	private Date date;
	
	public Statement() {}
	public Statement(String accountNumber,String description,Double amount,Date date) {
		this.accountNumber = accountNumber;
		this.description = description;
		this.amount = amount;
		this.date = date;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public String getDescription() {
		return description;
	}
	public Double getAmount() {
		return amount;
	}
	public Date getDate() {
		return date;
	}

}
