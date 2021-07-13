package com.example.demo.domain.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.domain.model.AccessAuthorization;
import com.example.demo.domain.repository.AccessAuthorizationRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

	private final AccessAuthorizationRepository authorizationRepository;

	@Autowired
	public AuthorizationServiceImpl(AccessAuthorizationRepository authorizationRepository) {
		super();
		this.authorizationRepository = authorizationRepository;
	}

	@Override
	public boolean isAuthorize(String roleName, String uri) {
		log.info("authorization service isAuthorizeMethod: args roleName: {}, uri: {}", roleName, uri);
		List<AccessAuthorization> authorizations = authorizationRepository.findByRoleName(roleName);
		boolean authorize = authorizations.stream().anyMatch(auth -> {
			log.info("request roleName:{} , database roleName: {}", roleName, auth.getRoleName());
			log.info("request uri:{} , database uri: {}", uri, auth.getUri());
			return Objects.equals(auth.getUri(), uri);
		});
		log.info("access OK?: {}", String.valueOf(authorize));
		return authorize;
	}

}
