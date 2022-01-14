package com.squadio.assessment.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.squadio.assessment.exceptions.ForbbidenAccessException;
import com.squadio.assessment.models.Account;
import com.squadio.assessment.models.StatementsInARange;
import com.squadio.assessment.services.AccountsService;

@RestController 
@RequestMapping("/accounts")
public class AccountsController {
	
	@Autowired
	private AccountsService accountsService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<Object> getUserAccounts(@PathVariable("userId") String userId) throws ForbbidenAccessException {
		return ResponseEntity.ok().body(this.accountsService.getUserAccounts(userId));
		
	} 
	@PostMapping("/statements")
	public ResponseEntity<Object> getAccountStatements(@RequestBody StatementsInARange statementsReqObject) throws ForbbidenAccessException {
		String s = "";
		return ResponseEntity.ok().body(this.accountsService.getAccountStatementsWithinARange(statementsReqObject));		
	} 
}
