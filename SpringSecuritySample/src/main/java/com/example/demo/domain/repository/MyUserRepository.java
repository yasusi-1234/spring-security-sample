package com.example.demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.MyUser;

public interface MyUserRepository extends JpaRepository<MyUser, String> {

}
