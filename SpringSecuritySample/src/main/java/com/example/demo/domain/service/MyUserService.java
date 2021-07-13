package com.example.demo.domain.service;

import java.util.List;

import com.example.demo.domain.model.MyUser;

public interface MyUserService {

	MyUser findByUserName(String userName);

	List<MyUser> findAll();
}
