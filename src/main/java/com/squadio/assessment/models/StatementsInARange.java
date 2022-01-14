package com.squadio.assessment.models;

public class StatementsInARange {
	private String accountId;
	private String fromDate;
	private String toDate;
	private Double fromAmount;
	private Double toAmount;
	
	public StatementsInARange() {}
	public StatementsInARange(String accountId, String fromDate, String toDate) {
		this.accountId = accountId;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}
	public StatementsInARange(String accountId, Double fromAmount, Double toAmount) {
		this.accountId = accountId;
		this.fromAmount = fromAmount;
		this.toAmount = toAmount;
	}
	public StatementsInARange(String accountId) {
		this.accountId = accountId;
	}
	
	
	public String getAccountId() {
		return accountId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public Double getFromAmount() {
		return fromAmount;
	}
	public Double getToAmount() {
		return toAmount;
	}

}
