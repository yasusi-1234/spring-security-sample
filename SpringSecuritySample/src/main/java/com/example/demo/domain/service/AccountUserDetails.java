package com.example.demo.domain.service;

import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import com.example.demo.domain.model.MyUser;

/* implements CredentialsConrainer 
 * SecurityConfigのconfigure(AuthenticationManagerBuilder auth)側での設定で有効化するメソッド、
 * eraseCredentials()をoverrideして設定側で利用するか決めてもらう感じ
 */
public class AccountUserDetails implements UserDetails, CredentialsContainer {

	private static final long serialVersionUID = 1L;

	private final MyUser myUser;

	private final String userName;
	private String password;
	private final String name;
	private final String roleName;

	public AccountUserDetails(MyUser myUser) {
		if (!StringUtils.hasLength(myUser.getUserName()) || !StringUtils.hasLength(myUser.getPassword())) {
			throw new IllegalArgumentException("Cannot pass null or empty values to constractor");
		}
		this.myUser = myUser;

		this.userName = myUser.getUserName();
		this.password = myUser.getPassword();
		this.name = myUser.getName();
		this.roleName = myUser.getRoleName();
	}

	public MyUser getUser() { // --- (1) Entityである MyUserを返却するメソッド
		return myUser;
	}

	public String getName() { // --- (2) nameを返却するメソッド
		return this.userName;
	}

	// --- (3) ユーザに与えられている権限リストを返却するメソッド
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_" + this.roleName);
	}

	// --- (4) 登録されているパスワードを返却するメソッド
	@Override
	public String getPassword() {
		return this.password;
	}

	// --- (5) ユーザ名を返却するメソッド
	@Override
	public String getUsername() {
		return this.userName;
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

	/*
	 * equalsとhashCodeを適切に実装していないと セッション管理の多重ログインチェックが機能しないためOverrideしている
	 */
	@Override
	public boolean equals(Object rhs) {
		if (!(rhs instanceof AccountUserDetails)) {
			return false;
		}
		return this.myUser.getUserName().equals(((AccountUserDetails) rhs).getUsername());
	}

	@Override
	public int hashCode() {
		return this.myUser.getUserName().hashCode();
	}

	/*
	 * CredentialsConrainer のメソッド
	 */
	@Override
	public void eraseCredentials() {
		this.password = null;
	}

}
