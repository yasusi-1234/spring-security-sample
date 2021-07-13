package com.example.demo.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.model.AccessAuthorization;

public interface AccessAuthorizationRepository extends JpaRepository<AccessAuthorization, Integer> {

	List<AccessAuthorization> findByRoleName(String roleName);
}
