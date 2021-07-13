package com.example.demo.domain.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.domain.model.MyUser;

public class AccountUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final MyUser myUser;

	public AccountUserDetails(MyUser myUser) {
		this.myUser = myUser;
	}

	public MyUser getUser() { // --- (1) Entityである MyUserを返却するメソッド
		return myUser;
	}

	public String getName() { // --- (2) nameを返却するメソッド
		return this.myUser.getName();
	}

	// --- (3) ユーザに与えられている権限リストを返却するメソッド
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + myUser.getRoleName());
	}

	// --- (4) 登録されているパスワードを返却するメソッド
	@Override
	public String getPassword() {
		return myUser.getPassword();
	}

	// --- (5) ユーザ名を返却するメソッド
	@Override
	public String getUsername() {
		return myUser.getUserName();
	}

	// --- (6) アカウントの有効期限の状態を判定するメソッド
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// --- (7) アカウントのロック状態を判定するメソッド
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// --- (8) 資格情報の有効期限の状態を判定するメソッド
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// --- (9) 有効なユーザかを判定するメソッド
	@Override
	public boolean isEnabled() {
		return true;
	}

}
