package com.squadio.assessment.services;

import org.springframework.http.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squadio.assessment.exceptions.ForbbidenAccessException;
import com.squadio.assessment.models.Account;
import com.squadio.assessment.models.Statement;
import com.squadio.assessment.models.StatementsInARange;

@Service  
public class AccountsService {
	
	private final RestTemplate restTemplate;
	private final AuthService authService;
	private final ObjectMapper mapper = new ObjectMapper();
	private final String baseUrl = "https://purple-fire-5350.getsandbox.com";
	
	@Autowired
	public AccountsService(RestTemplate restTemplate, AuthService authService) {
		this.restTemplate = restTemplate;
		this.authService = authService;
	}
	
	public List<Account> getUserAccounts(String userId){
		boolean isAdmin = this.authService.ifLoggedinUserIsAdmin();
		if(isAdmin) {
			return restTemplate.getForObject(baseUrl+"/accounts/"+userId, List.class);
		}else {
			boolean isRequestedAccForLoggedinUser = this.authService.checkIfUserIdIsTheLoggedinUser(userId);
			if(isRequestedAccForLoggedinUser) {
				return restTemplate.getForObject(baseUrl+"/accounts/"+userId, List.class);
			}else 
				throw new ForbbidenAccessException(); 
		}		
	}
	
	public List<Statement> getAccountStatements(StatementsInARange statementReqObject) {	
		
		List<Statement> deserializedStatements = null;
	
		
		ApiStatementReqObj reqObj = new ApiStatementReqObj(statementReqObject.getAccountId());
		
		boolean isAdmin = this.authService.ifLoggedinUserIsAdmin();
		
		if(isAdmin || checkIfStatementIsReqByRightAuthorities(statementReqObject)) {
			try {
				Object accountIdJsonObj = mapper.writeValueAsString(reqObj);
				HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> request = new HttpEntity<String>(accountIdJsonObj.toString(), headers);
				List<Statement> serializedStatements = restTemplate.postForObject(baseUrl+"/accounts/statements",request, List.class);
				 deserializedStatements = mapper.convertValue(serializedStatements, new TypeReference<List<Statement>>() { });
			}catch(Exception  e) {
				System.out.println(e);
			}			
		}else
			throw new ForbbidenAccessException();
		return deserializedStatements;			
	}
	public boolean checkIfStatementIsReqByRightAuthorities(StatementsInARange statementReqObject) {
		boolean accExist = false;
		String loggedinUserId = authService.getLoggedinUserId();
		List<Account> userAccounts = getUserAccounts(loggedinUserId);
		
		try {
			List<Account> accounts = mapper.convertValue(userAccounts, new TypeReference<List<Account>>() { });
			accExist = accounts.stream().anyMatch(acc -> acc.getId().equals(statementReqObject.getAccountId()));
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return accExist;
	}
	
	public List<Statement> getAccountStatementsWithinARange(StatementsInARange statementReqObject) {
		List<Statement> allStatements = getAccountStatements(statementReqObject);
		List<Statement> statementsInRange = new ArrayList<>();
		boolean filterByDateFlag = false;
		boolean filterByAmountFlag = false;
		boolean noParametersFilter = false; 
		
		if(statementReqObject.getFromAmount() != null && statementReqObject.getToAmount() != null) {
			noParametersFilter = false;
			filterByAmountFlag = true;
			filterByDateFlag = false;
		}else if(statementReqObject.getFromDate() != null && statementReqObject.getToDate() != null) {
			noParametersFilter = false;
			filterByAmountFlag = false;
			filterByDateFlag = true;
			
		}else { 
			noParametersFilter = true;
			filterByAmountFlag = false;
			filterByDateFlag = false;
		}
		
		if(noParametersFilter) {
			Optional<Statement> statement = allStatements.stream().findFirst();
			
			if(statement.isPresent()) {
				Date date3MonthsBack= getDate3MonthsBack(statement.get().getDate());
				statementsInRange = allStatements.stream().filter(state ->
						state.getDate().after(date3MonthsBack)).collect(Collectors.toList());
			}						
		}else if(filterByDateFlag) {
			Date fromDate = convertJsonStrdateToDate(statementReqObject.getFromDate());
			Date toDate = convertJsonStrdateToDate(statementReqObject.getToDate());
			
			statementsInRange = allStatements.stream().filter(state -> 
					state.getDate().before(fromDate)&& state.getDate().after(toDate)).collect(Collectors.toList());
			
		}else if(filterByAmountFlag) {
			
			statementsInRange = allStatements.stream().filter(state -> 
					state.getAmount() > statementReqObject.getFromAmount()  && 
					state.getAmount() <= statementReqObject.getToAmount()).collect(Collectors.toList());
		}
		return statementsInRange;	
	}
	public Date convertJsonStrdateToDate(String date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(date);
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	public Date getDate3MonthsBack(Date date) {
		try {			
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, -3);			
			return cal.getTime();
		}catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
}
class ApiStatementReqObj{
	private String accountId;
	
	public ApiStatementReqObj() {}
	public ApiStatementReqObj(String accountId){
		this.accountId = accountId;
	}
	public String getAccountId() {
		return accountId;
	}
}
