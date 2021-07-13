package com.example.demo.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.MyUser;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	private final MyUserService myUserService;

	@Autowired
	public AccountUserDetailsService(MyUserService myUserService) {
		super();
		this.myUserService = myUserService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || username.isBlank()) {
			throw new UsernameNotFoundException(username + " is not found");
		}

		MyUser myUser = myUserService.findByUserName(username);
		if (myUser.getUserName() == null) {
			throw new UsernameNotFoundException(username + " is not found");
		}
		AccountUserDetails accountUserDetails = new AccountUserDetails(myUser);

		return accountUserDetails;
	}

}
