package com.squadio.assessment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			cors()
			.and()
			.csrf().disable()
			/*.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/
			.authorizeRequests()
			.antMatchers("/login").permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin();
	}
	@Override
	@Bean 
	protected UserDetailsService userDetailsService() {
		UserDetails admin = User.builder()
				.username("Admin")
				.password(passwordEncoder.encode("admin"))
				.roles("ADMIN").build();
					
		UserDetails moha = User.builder()
				.username("Mohamed")
				.password(passwordEncoder.encode("user"))
				.roles("USER").build();
		
		UserDetails john = User.builder()
				.username("John")
				.password(passwordEncoder.encode("user"))
				.roles("USER").build();
		
		UserDetails kumar = User.builder()
				.username("kumar")
				.password(passwordEncoder.encode("user"))
				.roles("USER").build();
		
		return new InMemoryUserDetailsManager(admin,moha,john,kumar);
	}	
}
