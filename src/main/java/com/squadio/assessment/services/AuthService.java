package com.squadio.assessment.services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.squadio.assessment.models.AccountUser;

@Service 
public class AuthService {
	@Autowired
	private RestTemplate restTemplate;
	private final String baseUrl = "https://purple-fire-5350.getsandbox.com";
	
	public AccountUser getLoggedinUser() {
		String username = SecurityContextHolder. getContext(). getAuthentication().getName();		
		
		if(username != null) {
			return restTemplate.getForObject(baseUrl+"/users/"+username, AccountUser.class);
		}
		return new AccountUser("1","anonymous");
	}
	
	public boolean checkIfUserIdIsTheLoggedinUser(String reqUserId) {
		boolean userIdIsTheLoggedinUser = false;
		AccountUser user = getLoggedinUser();
		if(user.getId().equals(reqUserId))
			userIdIsTheLoggedinUser = true;
		else
			userIdIsTheLoggedinUser = false;
		return userIdIsTheLoggedinUser;
	}
	
	public boolean ifLoggedinUserIsAdmin() {
		Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder. getContext(). getAuthentication().getAuthorities();
		
		return authorities.stream().anyMatch(r-> r.getAuthority().equals("ROLE_ADMIN"));
		
	}
	public String getLoggedinUserId() {
		AccountUser accUser = getLoggedinUser();
		
		return accUser.getId();
	}

}
