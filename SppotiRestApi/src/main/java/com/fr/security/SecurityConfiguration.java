package com.fr.security;

import com.fr.commons.enumeration.UserRoleTypeEnum;
import com.fr.filter.CsrfHeaderFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by: Wail DJENANE On May 22, 2016
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
	
	/** Custom auth failure. */
	@Autowired
	private AuthFailure authFailure;
	
	/** Custom auth success. */
	@Autowired
	private AuthSuccess authSuccess;
	
	/** Security user details. */
	@Autowired
	private UserDetailsService userDetailService;
	
	/** Custom logout success handler. */
	@Autowired
	private LogoutSuccessHandler logoutSuccessHandler;
	
	/** Logout handler. */
	@Autowired
	private CustomLogoutHandler logoutHandler;
	
	/** Remember me token repository. */
	@Autowired
	private PersistentTokenRepository tokenRepository;
	
	/** Configure authentication provider. */
	@Autowired
	public void configureGlobalSecurity(final AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(this.userDetailService);
		auth.authenticationProvider(authenticationProvider());
	}
	
	/** Configure CROSS origin. */
	@Bean
	CorsConfigurationSource corsConfigurationSource()
	{
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Collections.singletonList("https://localhost:8000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception
	{
		
		http.
				cors().and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class).csrf()
				.csrfTokenRepository(csrfTokenRepository()).and().formLogin()
				.successHandler(savedRequestAwareAuthenticationSuccessHandler())
				//                .loginPage("/login")
				.usernameParameter("username").passwordParameter("password").successHandler(this.authSuccess)
				.failureHandler(this.authFailure).permitAll().and().rememberMe().rememberMeParameter("remember-me")
				.tokenRepository(this.tokenRepository).tokenValiditySeconds(86400).and().authorizeRequests()
				.antMatchers("/account/**", "/sport/**").permitAll().antMatchers("/admin/**")
				.hasRole(UserRoleTypeEnum.ADMIN.getUserProfileType()).antMatchers("/api/profile/**")
				.hasAnyRole(UserRoleTypeEnum.USER.getUserProfileType(), UserRoleTypeEnum.ADMIN.getUserProfileType())
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated().and().logout()
				.addLogoutHandler(this.logoutHandler).logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessHandler(this.logoutSuccessHandler);
	}
	
	/**
	 * Configure CSRF token.
	 *
	 * @return configured token.
	 */
	private CsrfTokenRepository csrfTokenRepository()
	{
		final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Bean
	public SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler()
	{
		
		final SavedRequestAwareAuthenticationSuccessHandler auth = new SavedRequestAwareAuthenticationSuccessHandler();
		auth.setTargetUrlParameter("/");
		return auth;
	}
	
	/**
	 * Init password encoder bean.
	 */
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Init auth provider.
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider()
	{
		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(this.userDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
	/**
	 * Init remember me token service.
	 */
	@Bean
	public PersistentTokenBasedRememberMeServices getPersistentTokenBasedRememberMeServices()
	{
		final PersistentTokenBasedRememberMeServices tokenBasedService = new PersistentTokenBasedRememberMeServices(
				"remember-me", this.userDetailService, this.tokenRepository);
		return tokenBasedService;
	}
	
	/**
	 * Init auth trust resolver.
	 */
	@Bean
	public AuthenticationTrustResolver getAuthenticationTrustResolver()
	{
		return new AuthenticationTrustResolverImpl();
	}
	
}
