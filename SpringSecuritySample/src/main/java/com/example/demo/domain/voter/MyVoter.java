package com.example.demo.domain.voter;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.example.demo.domain.service.AccountUserDetails;
import com.example.demo.domain.service.AuthorizationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MyVoter implements AccessDecisionVoter<FilterInvocation> {

	private final AuthorizationService authorizationService;

	@Autowired
	public MyVoter(AuthorizationService authorizationService) {
		super();
		this.authorizationService = authorizationService;
	}

	// ---(1) 投票処理が必要か不要か判定するメソッド
	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	// ---(1) 投票処理が必要か不要か判定するメソッド
	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	// ---(2) アクセス権を付与するかどうか投票するメソッド
	@Override
	public int vote(Authentication authentication, FilterInvocation filterInvocation,
			Collection<ConfigAttribute> attributes) {
		// --- (3) HttpServletRequestの取得
		HttpServletRequest request = filterInvocation.getHttpRequest();
		// --- (4) リクエストからURIを取得
		String uri = request.getRequestURI();
		// --- (5) 全てのRoleにアクセス許可されているか判定
		if (authorizationService.isAuthorize("*", uri)) {
			return ACCESS_GRANTED;
		}
		// --- (6) ユーザの識別情報の取得
		Object principal = authentication.getPrincipal();
		// ---(7) 取得した識別情報がAccountUserDetailsかどうか判定
		/*
		 * 取得した識別情報がAccountUserDetailsクラスかどうか判定する。
		 * 未認証の場合、識別情報がStringクラスで取得されるため、この判定処理を行わないとエラーが発生してしまう。
		 */
		if (!principal.getClass().equals(AccountUserDetails.class)) {
			return ACCESS_DENIED;
		}
		// ---(8) ユーザのRoleの取得
		String roleName = ((AccountUserDetails) principal).getUser().getRoleName();
		log.info("MyVoter vote Method: principal roleName is : {}", roleName);
		// ---(9) 取得したRoleがアクセス許可されているか判定
		if (authorizationService.isAuthorize(roleName, uri)) {
			return ACCESS_GRANTED;
		}

		return ACCESS_DENIED;
	}

}
