package com.example.demo.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterInvocation;

import com.example.demo.domain.service.AccountUserDetailsService;
import com.example.demo.domain.voter.MyVoter;

@Configuration
@EnableWebSecurity // --- (1) セキュリティーの設定クラスに付与
@EnableGlobalMethodSecurity(prePostEnabled = true) // --- (1) メソッド認可処理を有効化
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final AccountUserDetailsService accountUserDetailsService;

	private final AccessDecisionVoter<FilterInvocation> myVoter;

	@Autowired
	public WebSecurityConfig(AccountUserDetailsService accountUserDetailsService, MyVoter myVoter) {
		super();
		this.accountUserDetailsService = accountUserDetailsService;
		this.myVoter = myVoter;
	}

	public AccessDecisionManager createAccessDecisionManager() {
		// 追記 ---(1) 認可処理はAffirmativeBased、投票処理はMyVoterを使用する。
		return new AffirmativeBased(Arrays.asList(myVoter));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// --- (2) BCryptアルゴリズムを使用
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// AuthenticationManagerBuilderに、実装したUserDetailsServiceを設定する
		auth.eraseCredentials(true) // UserDetails側で設定したeraseCredentials()を実行するかしないかの設定
				// このメソッドを省略した場合はtrueの設定と同義
				.userDetailsService(accountUserDetailsService) // --- (3) 作成したUserDetailsServiceを設定
				.passwordEncoder(passwordEncoder()); // --- (2) パスワードのハッシュ化方法を指定(BCryptアルゴリズム)
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 認可の設定
		http.exceptionHandling().accessDeniedPage("/accessDeniedPage") // --- (2) アクセス拒否された時に遷移するパス
				.and().authorizeRequests().antMatchers("/**").authenticated() // 修正
				.accessDecisionManager(createAccessDecisionManager()); // ---(2) すべてのアクセスにおいて、認可処理の適用

//				.antMatchers("/loginForm").permitAll() // --- (4) /loginFormは、全ユーザからのアクセスを許可
//				.anyRequest().authenticated(); // --- (5) /loginForm以外は、認証を求める

		// ログインの設定
		http.formLogin() // --- (6) フォーム認証の有効化
				.loginPage("/loginForm") // --- (7) ログインフォームを表示するパス
				.loginProcessingUrl("/authenticate") // --- (8) フォーム認証処理のパス
				.usernameParameter("userName") // --- (9) ユーザ名のリクエストパラメータ名
				.passwordParameter("password") // --- (10) パスワードのリクエストパラメータ名
				/*
				 * 第二引数をtrueにすると、必ず第一引数のページへ遷移 第二引数がfalseの場合、 直前に指定されていたページへ遷移
				 */
				.defaultSuccessUrl("/home", false) // --- (11) 認証成功時に遷移するデフォルトのパス
				.failureUrl("/loginForm?error=true"); // --- (12) 認証失敗時に遷移するパス

		// ログアウト設定
		http.logout().logoutSuccessUrl("/loginForm") // --- (13) ログアウト成功時に遷移するパス
				.permitAll() // --- (14) 全ユーザに対してアクセスを許可
				.invalidateHttpSession(true) // HTTPSessionの破棄
				.deleteCookies("JSESSIONID"); // cookieからJsessionIDの破棄

	}

	@Override
	public void configure(WebSecurity web) {
		// --(15)静的リソースのアクセス許可設定
		web.ignoring().antMatchers("/css/**", "/js/**");
	}
}
