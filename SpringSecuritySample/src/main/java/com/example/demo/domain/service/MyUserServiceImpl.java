package com.example.demo.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.MyUser;
import com.example.demo.domain.repository.MyUserRepository;

@Service
public class MyUserServiceImpl implements MyUserService {

	private final MyUserRepository myUserRepository;

	@Autowired
	public MyUserServiceImpl(MyUserRepository myUserRepository) {
		super();
		this.myUserRepository = myUserRepository;
	}

	@Override
	public MyUser findByUserName(String userName) {
		return myUserRepository.findById(userName).orElse(new MyUser());
	}

	@Override
	public List<MyUser> findAll() {
		return myUserRepository.findAll();
	}

}
