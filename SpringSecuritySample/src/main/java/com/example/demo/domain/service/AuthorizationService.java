package com.example.demo.domain.service;

public interface AuthorizationService {

	boolean isAuthorize(String roleName, String uri);
}
